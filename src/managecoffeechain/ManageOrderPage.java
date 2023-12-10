/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managecoffeechain;

import services.*;
import models.*;
import utils.Utils;
import java.awt.Component;
import java.awt.Image;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author thangphan
 */
public class ManageOrderPage extends javax.swing.JFrame {
    static final int PRODUCT_ID_COLUMN_POSITION = 0;
    static final int PRODUCT_QUANTITY_COLUMN_POSITION = 1;
    static final int PRODUCT_NAME_COLUMN_POSITION = 0;
    static final int POINTS_EARN = 10;
    ProductService productService = new ProductService();
    UserService userService = new UserService();
    Utils utils = new Utils();
    int pointsEarnedOfUser = 0;

    /** Creates new form Home */
    public ManageOrderPage() {
        initComponents();
        
        productsTable.getColumnModel().getColumn(0).setPreferredWidth(200);
	displayProductsInProductsTable();
    }
    
	
	public Float getDiscountAmount(Float totalAmount, Float discountPercentage) {
		return totalAmount - (totalAmount * discountPercentage);
	}
	
	private boolean isUserExists(String phoneNumber) {
		User user = userService.getUserByPhoneNumber(phoneNumber);
		
		return user != null ? true : false;
	}
	
	private void autoFulfilNameOfUser(String phoneNumber) {
		System.out.println("autoFullFillNameOfUser");
		User user = userService.getUserByPhoneNumber(phoneNumber);
		System.out.println("Name of user: " + user.getName());
		nameOfUserTextField.setText(user.getName());
	}

	private void createUserWithoutPointsEarn(User user) {
		userService.insertUser(user);
	}

	private void updateOrCreateUserBasedOnConditionToEarnPoints(String phoneNumber, Float totalAmount) {
		boolean isApproveToEarnPoints = totalAmount >= 100000;
		boolean isUserExists = isUserExists(phoneNumber);
		String nameOfUser = nameOfUserTextField.getText();

		User user = null;
		
		System.out.println("isUserExists: " + isUserExists);
		System.out.println("isApproveToEarnPoints: " + isApproveToEarnPoints);
		
		if (isUserExists) {
			user = new User(nameOfUser, phoneNumber);

			if (isApproveToEarnPoints) {
				user.setPointsEarned(POINTS_EARN);
			}

			userService.updateUserPoints(user);
		} else {
			user = new User(nameOfUser, phoneNumber, isApproveToEarnPoints ? POINTS_EARN : 0);
			userService.insertUser(user);
		}
	}
	
    
    static class ImageRenderer extends JLabel implements TableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof ImageIcon) {
                setIcon((ImageIcon) value);
            } else {
                setIcon(null);
            }
            return this;
        }
    }
    
    public void supportRenderImageInTheOrderProductsTable() {
	    	productsOrderMenuTable.getColumnModel().getColumn(4).setCellRenderer(new ImageRenderer());
    }
    
    public void displayProductsInProductsTable() {
		ProductService productService = new ProductService();
		ArrayList<Product> products = productService.getProducts();
		DefaultTableModel model = (DefaultTableModel) productsOrderMenuTable.getModel();
		
		supportRenderImageInTheOrderProductsTable();
		int rowHeight = 150;
		productsOrderMenuTable.setRowHeight(rowHeight);
		
		for (Product product: products) {
			ImageIcon scaledImageIcon = createScaledImageIcon(product.getImage(), 120, 120);
			
			Object[] rowData = { product.getId(), product.getName(), product.getPrice(), product.getAddDate(), scaledImageIcon };
			model.addRow(rowData);
		}
	}
    
    private ImageIcon createScaledImageIcon(byte[] imageData, int width, int height) {
	ImageIcon imageIcon = new ImageIcon(imageData);
	Image scaledImage = imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
	return new ImageIcon(scaledImage);
    }
    
    public void printBill() {
        System.out.println("printBill");
        
        try {
            billTextPanel.setText("                         Quán cà phê hạnh phúc \n");
            billTextPanel.setText(billTextPanel.getText() + "\t123/ Quận 11, \n");
            billTextPanel.setText(billTextPanel.getText() + "\tĐường lớn \n");
            billTextPanel.setText(billTextPanel.getText() + "\t+83 0989193673, \n");
            billTextPanel.setText(billTextPanel.getText() + "----------------------------------------------------------------\n");
            billTextPanel.setText(billTextPanel.getText() + "Sản phẩm \tSố lượng \tGiá \n");
            billTextPanel.setText(billTextPanel.getText() + "----------------------------------------------------------------\n");
            
            DefaultTableModel df = (DefaultTableModel) productsTable.getModel();
            for (int i = 0; i < productsTable.getRowCount(); i++) {
                
                String name = df.getValueAt(i, 0).toString();
                String qt = df.getValueAt(i, 1).toString();
                String prc = df.getValueAt(i, 2).toString();
                
                billTextPanel.setText(billTextPanel.getText() + name+"\t"+qt+"\t"+prc+" \n");
                
            }
            billTextPanel.setText(billTextPanel.getText() + "----------------------------------------------------------------\n");
            billTextPanel.setText(billTextPanel.getText() + "Tổng phụ :\t"+totalTextField.getText()+"\n");
            billTextPanel.setText(billTextPanel.getText() + "Tiền đã nhận :\t"+cashReceiveTextField.getText()+"\n");
            billTextPanel.setText(billTextPanel.getText() + "====================================\n");
            billTextPanel.setText(billTextPanel.getText() +"                     Cảm ơn bạn đã ghe thăm shop chúng tôi...!"+"\n");
            billTextPanel.setText(billTextPanel.getText() + "----------------------------------------------------------------\n");
            billTextPanel.setText(billTextPanel.getText() +"                     Bản quyển thuộc quán cà phê hạnh phúcs"+"\n");
            
            billTextPanel.print();
        } catch (PrinterException ex) {
            
            Logger.getLogger(ManageOrderPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getSubTotal() {
        int numOfRows = productsTable.getRowCount();
        double total = 0;
        
        for (int i = 0; i < numOfRows; i++) {
            String price = productsTable.getValueAt(i, 2).toString();
            
            total += Double.valueOf(price);
        }
        
	return String.valueOf(total);
    }
    
    public String getTotal() {
        int numOfRows = productsTable.getRowCount();
        double total = 0;
        
        for (int i = 0; i < numOfRows; i++) {
            String price = productsTable.getValueAt(i, 2).toString();
            String quantity = productsTable.getValueAt(i, 1).toString();
            
            total += Double.valueOf(price) * Double.valueOf(quantity);
        }
        
        return String.valueOf(total);
    }
    
    public void setTotalAndSubTotalData(String subTotal, String total) {
	System.out.println("setTotalAndSubTotalData");
        subTotalTextField.setText(subTotal);
        totalTextField.setText(total);
    }
    
    public void addToTable(Product product) {
	System.out.println("addToTable");
        String quantity = JOptionPane.showInputDialog(null, "Vui lòng nhập số lượng sản phẩm");
        Float totalQuantity = Float.valueOf(quantity);
        Float totalPrice = product.getPrice() * totalQuantity;
        
        System.out.println("totalPrice: " + totalPrice);
                
        // Add product to cart
        DefaultTableModel tableModel = (DefaultTableModel) productsTable.getModel();
        Vector vector = new Vector();
        vector.add(product.getName());
        vector.add(totalQuantity);
        vector.add(totalPrice);
        tableModel.addRow(vector);
        
        setTotalAndSubTotalData(getSubTotal(), getTotal());
    }

    @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                jPanel1 = new javax.swing.JPanel();
                jPanel2 = new javax.swing.JPanel();
                jScrollPane3 = new javax.swing.JScrollPane();
                productsOrderMenuTable = new javax.swing.JTable();
                bannerImageLabel = new javax.swing.JLabel();
                jPanel3 = new javax.swing.JPanel();
                jScrollPane1 = new javax.swing.JScrollPane();
                productsTable = new javax.swing.JTable();
                jScrollPane2 = new javax.swing.JScrollPane();
                billTextPanel = new javax.swing.JTextPane();
                jPanel4 = new javax.swing.JPanel();
                jLabel1 = new javax.swing.JLabel();
                cashReceiveTextField = new javax.swing.JTextField();
                jLabel2 = new javax.swing.JLabel();
                jLabel3 = new javax.swing.JLabel();
                subTotalTextField = new javax.swing.JTextField();
                totalTextField = new javax.swing.JTextField();
                jButton1 = new javax.swing.JButton();
                payButton = new javax.swing.JButton();
                newOrderButton = new javax.swing.JButton();
                jLabel4 = new javax.swing.JLabel();
                nameOfUserTextField = new javax.swing.JTextField();
                jLabel5 = new javax.swing.JLabel();
                userPhoneNumberTextField = new javax.swing.JTextField();
                discountLabel = new javax.swing.JLabel();

                setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

                javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
                jPanel2.setLayout(jPanel2Layout);
                jPanel2Layout.setHorizontalGroup(
                        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 541, Short.MAX_VALUE)
                );
                jPanel2Layout.setVerticalGroup(
                        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
                );

                productsOrderMenuTable.setModel(new javax.swing.table.DefaultTableModel(
                        new Object [][] {

                        },
                        new String [] {
                                "Id", "Tên", "Giá", "Ngày", "Hình"
                        }
                ));
                productsOrderMenuTable.setSize(new java.awt.Dimension(450, 164));
                productsOrderMenuTable.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                productsOrderMenuTableMouseClicked(evt);
                        }
                });
                jScrollPane3.setViewportView(productsOrderMenuTable);

                bannerImageLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/managecoffeechain/images/coffee-shop-banner-chrismas.jpeg"))); // NOI18N

                javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                jPanel1Layout.setHorizontalGroup(
                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(241, 241, 241))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 645, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(bannerImageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 645, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                );
                jPanel1Layout.setVerticalGroup(
                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 679, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bannerImageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(23, 23, 23))
                );

                productsTable.setModel(new javax.swing.table.DefaultTableModel(
                        new Object [][] {

                        },
                        new String [] {
                                "Tên sản phẩm", "Số lượng", "Giá"
                        }
                ));
                productsTable.setRowHeight(24);
                jScrollPane1.setViewportView(productsTable);

                jScrollPane2.setViewportView(billTextPanel);

                javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
                jPanel3.setLayout(jPanel3Layout);
                jPanel3Layout.setHorizontalGroup(
                        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
                jPanel3Layout.setVerticalGroup(
                        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE))
                                .addContainerGap())
                );

                jLabel1.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 24)); // NOI18N
                jLabel1.setText("Tiền nhận: ");

                cashReceiveTextField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 24)); // NOI18N
                cashReceiveTextField.setText("0");
                cashReceiveTextField.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                cashReceiveTextFieldActionPerformed(evt);
                        }
                });
                cashReceiveTextField.addKeyListener(new java.awt.event.KeyAdapter() {
                        public void keyReleased(java.awt.event.KeyEvent evt) {
                                cashReceiveTextFieldKeyReleased(evt);
                        }
                });

                jLabel2.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 24)); // NOI18N
                jLabel2.setText("Tên khách hàng:");

                jLabel3.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 24)); // NOI18N
                jLabel3.setText("Tổng tiền:");

                subTotalTextField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 24)); // NOI18N
                subTotalTextField.setText("0");
                subTotalTextField.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                subTotalTextFieldActionPerformed(evt);
                        }
                });

                totalTextField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 24)); // NOI18N
                totalTextField.setText("0");
                totalTextField.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                totalTextFieldActionPerformed(evt);
                        }
                });

                jButton1.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 24)); // NOI18N
                jButton1.setText("In hoá đơn");
                jButton1.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButton1ActionPerformed(evt);
                        }
                });

                payButton.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 24)); // NOI18N
                payButton.setText("Thanh toán");
                payButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                payButtonActionPerformed(evt);
                        }
                });

                newOrderButton.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 24)); // NOI18N
                newOrderButton.setText("Tạo đơn mới");
                newOrderButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                createNewOrderActionPerformed(evt);
                        }
                });

                jLabel4.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 24)); // NOI18N
                jLabel4.setText("Tổng phụ:");

                nameOfUserTextField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 24)); // NOI18N
                nameOfUserTextField.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                nameOfUserTextFieldActionPerformed(evt);
                        }
                });

                jLabel5.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 24)); // NOI18N
                jLabel5.setText("SĐT khách:");

                userPhoneNumberTextField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 24)); // NOI18N
                userPhoneNumberTextField.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                userPhoneNumberTextFieldActionPerformed(evt);
                        }
                });
                userPhoneNumberTextField.addKeyListener(new java.awt.event.KeyAdapter() {
                        public void keyReleased(java.awt.event.KeyEvent evt) {
                                userPhoneNumberTextFieldKeyReleased(evt);
                        }
                });

                discountLabel.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
                discountLabel.setForeground(new java.awt.Color(255, 102, 102));

                javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
                jPanel4.setLayout(jPanel4Layout);
                jPanel4Layout.setHorizontalGroup(
                        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(newOrderButton, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addComponent(jLabel1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cashReceiveTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(subTotalTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                                .addComponent(totalTextField)
                                                                .addGap(2, 2, 2)))))
                                .addGap(26, 26, 26)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addComponent(jLabel2)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(nameOfUserTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addComponent(jLabel5)
                                                .addGap(18, 18, 18)
                                                .addComponent(userPhoneNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(discountLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(jPanel4Layout.createSequentialGroup()
                                                        .addComponent(payButton, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(121, Short.MAX_VALUE))
                );
                jPanel4Layout.setVerticalGroup(
                        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(subTotalTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel5)
                                        .addComponent(userPhoneNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(totalTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel2)
                                        .addComponent(nameOfUserTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(15, 15, 15)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(cashReceiveTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(discountLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(newOrderButton, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(payButton, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(102, Short.MAX_VALUE))
                );

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 662, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                pack();
        }// </editor-fold>//GEN-END:initComponents

    private void createNewOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createNewOrderActionPerformed
        DefaultTableModel tb = (DefaultTableModel) productsTable.getModel();
        tb.setRowCount(0);
        
        billTextPanel.setText("");
        subTotalTextField.setText("");
        totalTextField.setText("");
        cashReceiveTextField.setText("");
    }//GEN-LAST:event_createNewOrderActionPerformed

        private void payButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payButtonActionPerformed
                Double cashReceive = Double.valueOf(cashReceiveTextField.getText());
                if (cashReceive < 0) {
                        String cashFromClient = JOptionPane.showInputDialog(null, "Số tiền nhân không được nhó hơn tiền cần thanh toán vui lòng nhập lại");
                        Double cashRequired = Double.valueOf(cashFromClient);
                        Double total = Double.valueOf(getTotal());

                        if (cashRequired > total) {
                                Double balance = Double.valueOf(cashRequired) - Double.valueOf(total);
                        }

                        return;
                }

                Float total = Float.valueOf(getTotal());
		if (pointsEarnedOfUser != 0) {
			Float discountPercentage = Float.valueOf(pointsEarnedOfUser) / 100;
			total = getDiscountAmount(total, discountPercentage);
			System.out.println("total after discount: " + total);
			
			// need to delete the earns point after using it
			String phoneNumber = userPhoneNumberTextField.getText();
			User user = userService.getUserByPhoneNumber(phoneNumber);
			user.setPointsEarned(0);
			userService.updateUserPoints(user);
		}
                Float cashRequired = Float.valueOf(cashReceiveTextField.getText());
                Float balance = cashRequired - total;
		User user = null;
		
		System.out.println("total from text field" + total);
		String phoneNumber = userPhoneNumberTextField.getText();
		String nameOfUser = nameOfUserTextField.getText();

		updateOrCreateUserBasedOnConditionToEarnPoints(phoneNumber, total);
                printBill();
		
		OrderService orderService = new OrderService();
//		int selectedRowIndex = productsOrderMenuTable.getSelectedRow();
//		int productId = (int) productsOrderMenuTable.getValueAt(selectedRowIndex, PRODUCT_ID_COLUMN_POSITION);
//		Product product = productService.getProductById(productId);
//		addToTable(product);

		user = userService.getUserByPhoneNumber(phoneNumber);
		Order order = new Order(total, user.getId());
		int orderId = orderService.insertOrder(order);
		OrderProductService orderProductService = new OrderProductService();
	
		
		int itemsCount = productsTable.getRowCount();
		
		System.out.println("items count: " + itemsCount);
		
		for (int i = 0; i < itemsCount; i++) {
			System.out.println("Loop me!");
			String productName = String.valueOf(productsTable.getValueAt(i, PRODUCT_NAME_COLUMN_POSITION));
			String quatityValue = String.valueOf(productsTable.getValueAt(i, PRODUCT_QUANTITY_COLUMN_POSITION));
			String[] quantityArrayOfStrings = quatityValue.split("\\.");
			System.out.println("quantityArrayOfStrings: " + quantityArrayOfStrings[0]);
			int quantity = Integer.valueOf(quantityArrayOfStrings[0]);
			
			Product product = productService.getProductByName(productName);
			
			System.out.println("orderId: " + orderId);
			System.out.println("productId: " + product.getId());
			System.out.println("quantity: " + quantity);
			OrderProduct orderProduct = new OrderProduct(orderId, product.getId(), quantity);
			orderProductService.insertOrderProduct(orderProduct);
		}
		
		JOptionPane.showMessageDialog(null, "Đơn hàng đã thanh toán thành công");
        }//GEN-LAST:event_payButtonActionPerformed

        private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
                // TODO add your handling code here:
        }//GEN-LAST:event_jButton1ActionPerformed

        private void totalTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalTextFieldActionPerformed
                // TODO add your handling code here:
        }//GEN-LAST:event_totalTextFieldActionPerformed

        private void subTotalTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subTotalTextFieldActionPerformed
                // TODO add your handling code here:
        }//GEN-LAST:event_subTotalTextFieldActionPerformed

        private void cashReceiveTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cashReceiveTextFieldKeyReleased

        }//GEN-LAST:event_cashReceiveTextFieldKeyReleased

        private void cashReceiveTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cashReceiveTextFieldActionPerformed

        }//GEN-LAST:event_cashReceiveTextFieldActionPerformed

        private void productsOrderMenuTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productsOrderMenuTableMouseClicked
		int selectedRowIndex = productsOrderMenuTable.getSelectedRow();
		int productId = (int) productsOrderMenuTable.getValueAt(selectedRowIndex, PRODUCT_ID_COLUMN_POSITION);
		Product product = productService.getProductById(productId);
		addToTable(product);
        }//GEN-LAST:event_productsOrderMenuTableMouseClicked

        private void nameOfUserTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameOfUserTextFieldActionPerformed
                // TODO add your handling code here:
        }//GEN-LAST:event_nameOfUserTextFieldActionPerformed

        private void userPhoneNumberTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userPhoneNumberTextFieldActionPerformed
                // TODO add your handling code here:
        }//GEN-LAST:event_userPhoneNumberTextFieldActionPerformed

	
        private void userPhoneNumberTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_userPhoneNumberTextFieldKeyReleased
		String phoneNumber = userPhoneNumberTextField.getText();
		if (isUserExists(phoneNumber)) {
			autoFulfilNameOfUser(phoneNumber);
			User user = userService.getUserByPhoneNumber(phoneNumber);
			System.out.println("points earned: " + user.getPointsEarned());
			if (user.getPointsEarned() >= 10) {
				pointsEarnedOfUser = user.getPointsEarned();
				Float total = Float.valueOf(totalTextField.getText());
				Float discountPercentage = Float.valueOf(pointsEarnedOfUser) / 100;
				Float totalAmountAfterDiscount = getDiscountAmount(total, discountPercentage);
				totalTextField.setText(String.valueOf(totalAmountAfterDiscount));
				
				discountLabel.setText("Khách hàng này được giảm giá 10%.");
			}
		}
        }//GEN-LAST:event_userPhoneNumberTextFieldKeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ManageOrderPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManageOrderPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManageOrderPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManageOrderPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManageOrderPage().setVisible(true);
            }
        });
    }

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JLabel bannerImageLabel;
        private javax.swing.JTextPane billTextPanel;
        private javax.swing.JTextField cashReceiveTextField;
        private javax.swing.JLabel discountLabel;
        private javax.swing.JButton jButton1;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JLabel jLabel3;
        private javax.swing.JLabel jLabel4;
        private javax.swing.JLabel jLabel5;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JPanel jPanel2;
        private javax.swing.JPanel jPanel3;
        private javax.swing.JPanel jPanel4;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JScrollPane jScrollPane2;
        private javax.swing.JScrollPane jScrollPane3;
        private javax.swing.JTextField nameOfUserTextField;
        private javax.swing.JButton newOrderButton;
        private javax.swing.JButton payButton;
        private javax.swing.JTable productsOrderMenuTable;
        private javax.swing.JTable productsTable;
        private javax.swing.JTextField subTotalTextField;
        private javax.swing.JTextField totalTextField;
        private javax.swing.JTextField userPhoneNumberTextField;
        // End of variables declaration//GEN-END:variables

}
