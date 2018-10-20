package com.belyabl9.client.ui;

import com.belyabl9.client.Client;
import com.belyabl9.api.Department;
import com.belyabl9.api.User;
import com.belyabl9.client.ui.form.DepartmentForm;
import com.belyabl9.client.ui.form.UserForm;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Set;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class AdminPanel extends JPanel implements ActionListener {

    private List<Department> departments;
    
    private DynamicTree treePanel;
    private static AdminPanel adminPanel = new AdminPanel();
    
    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        f.getContentPane().add(adminPanel);
        f.pack();
        f.setVisible(true);
    }
    
    
    private AdminPanel() {
        super(new BorderLayout());
    
        treePanel = new DynamicTree();
        populateTree(treePanel);
        treePanel.setPreferredSize(new Dimension(300, 150));
        add(treePanel, BorderLayout.CENTER);
      }
    
    public static AdminPanel getInstance() {
      return adminPanel;
    }
  
    public void populateTree(DynamicTree treePanel) {
        departments = getAllDepartments();
        DefaultMutableTreeNode d;
        for (Department department : departments) {
            d = treePanel.addObject(null, department);
            Set<User> users = department.getUsers();
            for (User user : users) {
                treePanel.addObject(d, user);
            }
        }
    }

    class DynamicTree extends JPanel {
        protected DefaultMutableTreeNode rootNode;
        protected DefaultTreeModel treeModel;
        protected JTree tree;
    
        public DynamicTree() {
            super(new GridLayout(1, 0));
    
            rootNode = new DefaultMutableTreeNode("Departments");
            treeModel = new DefaultTreeModel(rootNode,true);
            tree = new JTree(treeModel);
            tree.setEditable(true);
            tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            tree.setShowsRootHandles(true);
            tree.addMouseListener ( new MouseAdapter () {
                public void mousePressed ( MouseEvent e ) {
                    if ( SwingUtilities.isRightMouseButton ( e ) ) {
                        TreePath path = tree.getPathForLocation ( e.getX (), e.getY () );
                        Rectangle pathBounds = tree.getUI ().getPathBounds ( tree, path );
                        TreePath parentPath = tree.getSelectionPath();
                        if (pathBounds != null && pathBounds.contains ( e.getX (), e.getY () )) {
                            JPopupMenu menu = new JPopupMenu ();                    
                            if (parentPath.getPathCount() != 3 ) {
                            JMenuItem addMenuItem = new JMenuItem("Add");
                            addMenuItem.addActionListener(actionEvent -> {
                                DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
                                if(currentNode != null){ 	    
                                    Object nodeInfo = currentNode.getUserObject(); 
                                   if (nodeInfo instanceof Department) {
                                       Department department = (Department)nodeInfo;
                                       UserForm userForm = new UserForm(department);
                                       userForm.setVisible(true);
                                       userForm.addPropertyChangeListener(result -> {
                                           User user = (User) result.getNewValue();
                                           treePanel.addObject(user);
                                       });                   	                            		                        		      
                                   } else {
                                       DepartmentForm departmentForm;
                                       if (tree.getSelectionPath().getPathCount() != 1) {
                                           Department department = (Department) nodeInfo;
                                           departmentForm = new DepartmentForm(department);
                                       } else {
                                           departmentForm = new DepartmentForm();
                                       }
                                       
                                       departmentForm.setVisible(true);
                                       departmentForm.addPropertyChangeListener(result -> {
                                           Department department = (Department) result.getNewValue();
                                           treePanel.addObject(department);
                                       });                    		       
                                   }	        
                              }
                            });
                            menu.add (addMenuItem);
                           }
                            
                            if (parentPath.getPathCount() != 1) {
                                JMenuItem deleteMenuItem = new JMenuItem("Delete");
                                deleteMenuItem.addActionListener(actionEvent -> {
                                    DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                                    if (currentNode != null) {
                                        Object nodeInfo = currentNode.getUserObject();
                                       if (nodeInfo instanceof Department) {
                                           Department department = (Department)nodeInfo;
                                           treePanel.removeCurrentNode();
                                           deleteDepartmentFromBD (department.getId());
                                       }
                                       if (nodeInfo instanceof User) {
                                           User user = (User)nodeInfo;
                                           treePanel.removeCurrentNode();
                                           deleteUserFromBD(user.getId());                       	    	                 		       
                                       }	        
                                    }
                                });
                                menu.add(deleteMenuItem);
                            }
                           
                            if (parentPath.getPathCount() != 1) {
                                JMenuItem updateMenuItem = new JMenuItem("Update");
                                updateMenuItem.addActionListener(actionEvent -> {
                                    DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
                                    if (currentNode != null) {
                                        Object nodeInfo = currentNode.getUserObject(); 
                                       if (nodeInfo instanceof User){
                                           User user = (User)nodeInfo;
                                           UserForm userForm = new UserForm(user, (Department) currentNode.getParent());
                                           userForm.setVisible(true);
                                           userForm.addPropertyChangeListener(result -> {
                                              User user1 = (User) result.getNewValue();
                                              treePanel.updateObject(user1);
                                           });                   	                            		                        		      
                                       }
                                       if (nodeInfo instanceof Department) {
                                           Department department = (Department) nodeInfo;
                                           DepartmentForm departmentForm = new DepartmentForm(department);
                                           departmentForm.setName(department.getName());
                                           departmentForm.setVisible(true);
                                           departmentForm.addPropertyChangeListener(result -> {
                                               Department department1 = (Department) result.getNewValue();
                                               treePanel.updateObject(department1);
                                           });
                                       }
                                    }
                                });
                                menu.add (updateMenuItem);
                            }
                            menu.show (tree, pathBounds.x, pathBounds.y + pathBounds.height );   
                        }
                    }
                }
            });
            JScrollPane scrollPane = new JScrollPane(tree);
            add(scrollPane);
        }
       // treeModel.reload();
      
        /** Remove the currently selected node. */
        public void removeCurrentNode() {
            TreePath currentSelection = tree.getSelectionPath();
            if (currentSelection != null) {
                DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) (currentSelection.getLastPathComponent());
                MutableTreeNode parent = (MutableTreeNode) (currentNode.getParent());
                if (parent != null) {
                    treeModel.removeNodeFromParent(currentNode);
                    return;
                }
            }
        }
    
        /** Add child to the currently selected node. */
        public DefaultMutableTreeNode addObject(Object child) {
            DefaultMutableTreeNode parentNode = null;
            TreePath parentPath = tree.getSelectionPath();
            
            if (parentPath == null) {
                parentNode = rootNode;
            } else {
                parentNode = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
            }
            return addObject(parentNode, child, true);
        }
      
      public void updateObject(Object child) {
            DefaultMutableTreeNode parentNode, parentNodeRem;
            TreePath parentPath = tree.getSelectionPath();
            
            if (parentPath.getPathCount() == 2) {	    	
                parentNodeRem = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
                DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
                if (currentNode != null) {
                    Object nodeInfo = currentNode.getUserObject();
                    if (nodeInfo instanceof Department) {
                       Department dep = (Department) nodeInfo;
                       dep.setName(((Department) child).getName());
                       DefaultMutableTreeNode d = treePanel.addObject(null, child);
                       Set<User> users = dep.getUsers();
                       for (User user : users) {
                           treePanel.addObject(d, user);
                       }
                       treeModel.removeNodeFromParent(parentNodeRem);   
                    }
                }
            } else {
                parentNode = (DefaultMutableTreeNode) (parentPath.getPathComponent(1));
                parentNodeRem = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
                treeModel.removeNodeFromParent(parentNodeRem);
                addObject(parentNode, child, true);
            }	   
          }
    
        public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent, Object child) {
            return addObject(parent, child, false);
        }
    
        public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent, Object child, boolean shouldBeVisible) {
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
        
            if (parent == null) {
                parent = rootNode;
            }
        
            // It is key to invoke this on the TreeModel, and NOT DefaultMutableTreeNode
            treeModel.insertNodeInto(childNode, parent, parent.getChildCount());
            
            // Make sure the user can see the lovely new node.
            if (shouldBeVisible) {
                tree.scrollPathToVisible(new TreePath(childNode.getPath()));
            }
            return childNode;
        }
    }

    private void deleteDepartmentFromBD(long id) {
        
        
    }
    private void deleteUserFromBD(long id) {
        
        
    }
    @Override
    public void actionPerformed(ActionEvent arg0) {
        	
    }
    
    public List<Department> getAllDepartments(){
        return Client.getDepartments();
    }

}



     
