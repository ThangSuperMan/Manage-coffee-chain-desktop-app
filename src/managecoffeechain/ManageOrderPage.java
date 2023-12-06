/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managecoffeechain;

import com.sun.corba.se.impl.orbutil.closure.Constant;
import com.sun.xml.internal.fastinfoset.tools.StAX2SAXReader;
import services.ProductService;
import utils.Utils;
import java.awt.Component;
import java.awt.Image;
import java.awt.print.PrinterException;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import models.Product;

/**
 *
 * @author thangphan
 */
public class ManageOrderPage extends javax.swing.JFrame {
    static final int PRODUCT_ID_COLUMN_POSITION = 0;
    ProductService productService = new ProductService();
    Utils utils = new Utils();

    /** Creates new form Home */
    public ManageOrderPage() {
        initComponents();
        
        productsTable.getColumnModel().getColumn(0).setPreferredWidth(200);
	displayProductsInProductsTable();
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
	    	productsOrderTable.getColumnModel().getColumn(4).setCellRenderer(new ImageRenderer());
    }
    
    public void displayProductsInProductsTable() {
		ProductService productService = new ProductService();
		ArrayList<Product> products = productService.getProducts();
		DefaultTableModel model = (DefaultTableModel) productsOrderTable.getModel();
		
		supportRenderImageInTheOrderProductsTable();
		int rowHeight = 150;
		productsOrderTable.setRowHeight(rowHeight);
		
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
            billTextPanel.setText(billTextPanel.getText() + "Tiền dư :\t"+balanceTextField.getText()+"\n");
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
        total += Double.valueOf(taxTextField.getText());
        
        return String.valueOf(total);
    }
    
    public void setTotalAndSubTotalData(String subTotal, String total) {
        subTotalTextField.setText(subTotal);
        totalTextField.setText(total);
    }
    
    public void addToTable(Product product) {
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
                jButton4 = new javax.swing.JButton();
                jButton3 = new javax.swing.JButton();
                jButton5 = new javax.swing.JButton();
                newOrderButton = new javax.swing.JButton();
                jScrollPane3 = new javax.swing.JScrollPane();
                productsOrderTable = new javax.swing.JTable();
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
                jLabel4 = new javax.swing.JLabel();
                taxTextField = new javax.swing.JTextField();
                payButton = new javax.swing.JButton();
                jLabel5 = new javax.swing.JLabel();
                balanceTextField = new javax.swing.JTextField();

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

                jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/managecoffeechain/images/banh_mousse_dao.png"))); // NOI18N
                jButton4.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButton4ActionPerformed(evt);
                        }
                });

                jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/managecoffeechain/images/banh_mousse_cacao.png"))); // NOI18N
                jButton3.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButton3ActionPerformed(evt);
                        }
                });

                jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/managecoffeechain/images/banh_mousse_dao.png"))); // NOI18N
                jButton5.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                productActionPerformed(evt);
                        }
                });

                newOrderButton.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 24)); // NOI18N
                newOrderButton.setText("Tạo đơn mới");
                newOrderButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                createNewOrderActionPerformed(evt);
                        }
                });

                productsOrderTable.setModel(new javax.swing.table.DefaultTableModel(
                        new Object [][] {

                        },
                        new String [] {
                                "Id", "Tên", "Giá", "Ngày", "Hình"
                        }
                ));
                productsOrderTable.setSize(new java.awt.Dimension(450, 164));
                productsOrderTable.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                productsOrderTableMouseClicked(evt);
                        }
                });
                jScrollPane3.setViewportView(productsOrderTable);

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
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 645, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(newOrderButton, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(bannerImageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 645, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                );
                jPanel1Layout.setVerticalGroup(
                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bannerImageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(388, 388, 388)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(newOrderButton, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(376, 376, 376))
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
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                jLabel2.setText("Tổng phụ:");

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

                jLabel4.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 24)); // NOI18N
                jLabel4.setText("Thuế(vnd):");

                taxTextField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 24)); // NOI18N
                taxTextField.setText("0");
                taxTextField.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                taxTextFieldActionPerformed(evt);
                        }
                });
                taxTextField.addKeyListener(new java.awt.event.KeyAdapter() {
                        public void keyReleased(java.awt.event.KeyEvent evt) {
                                taxTextFieldKeyReleased(evt);
                        }
                });

                payButton.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 24)); // NOI18N
                payButton.setText("Thanh toán");
                payButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                payButtonActionPerformed(evt);
                        }
                });

                jLabel5.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 24)); // NOI18N
                jLabel5.setText("Tiền dư:");

                balanceTextField.setFont(new java.awt.Font(".AppleSystemUIFont", 0, 24)); // NOI18N
                balanceTextField.setText("0");
                balanceTextField.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                balanceTextField1ActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
                jPanel4.setLayout(jPanel4Layout);
                jPanel4Layout.setHorizontalGroup(
                        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel1))
                                                .addGap(122, 122, 122))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(cashReceiveTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                                                .addComponent(totalTextField, javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(subTotalTextField, javax.swing.GroupLayout.Alignment.TRAILING)))
                                .addGap(31, 31, 31)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel4)
                                                        .addComponent(jLabel5))
                                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                                .addGap(7, 7, 7)
                                                                .addComponent(balanceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(taxTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addComponent(payButton, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                                                .addComponent(jButton1)
                                                .addGap(23, 23, 23))))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(16, 16, 16)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap(565, Short.MAX_VALUE)))
                );
                jPanel4Layout.setVerticalGroup(
                        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(subTotalTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel4))
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addGap(4, 4, 4)
                                                .addComponent(taxTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addGap(14, 14, 14)
                                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                .addComponent(jLabel3)
                                                                .addComponent(totalTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(jLabel5))
                                                .addGap(18, 18, 18)
                                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel1)
                                                        .addComponent(cashReceiveTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addGap(12, 12, 12)
                                                .addComponent(balanceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(30, 30, 30)
                                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(payButton, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(70, Short.MAX_VALUE))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(16, 16, 16)
                                        .addComponent(jLabel2)
                                        .addContainerGap(209, Short.MAX_VALUE)))
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
                                .addGap(18, 18, 18)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(14, 14, 14))
                );

                pack();
        }// </editor-fold>//GEN-END:initComponents

    private void productActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_productActionPerformed
	System.out.println("clicked a row");
//        addToTable(name, price);
	
    }//GEN-LAST:event_productActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void createNewOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createNewOrderActionPerformed
        DefaultTableModel tb = (DefaultTableModel) productsTable.getModel();
        tb.setRowCount(0);
        
        billTextPanel.setText("");
        subTotalTextField.setText("");
        totalTextField.setText("");
        taxTextField.setText("");
        balanceTextField.setText("");
        cashReceiveTextField.setText("");
    }//GEN-LAST:event_createNewOrderActionPerformed

        private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
                String name = "Bánh dẻo";
                Double price = 50.000;
//                addToTable(name, price);
        }//GEN-LAST:event_jButton3ActionPerformed

        private void balanceTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_balanceTextField1ActionPerformed
                // TODO add your handling code here:
        }//GEN-LAST:event_balanceTextField1ActionPerformed

        private void payButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payButtonActionPerformed
                Double cashReceive = Double.valueOf(cashReceiveTextField.getText());
                if (cashReceive < 0) {
                        String cashFromClient = JOptionPane.showInputDialog(null, "Số tiền nhân không được nhó hơn tiền cần thanh toán vui lòng nhập lại");
                        Double cashRequired = Double.valueOf(cashFromClient);
                        Double total = Double.valueOf(getTotal());

                        if (cashRequired > total) {
                                Double balance = Double.valueOf(cashRequired) - Double.valueOf(total);
                                balanceTextField.setText(String.valueOf(balance));
                        }

                        balanceTextField.setText(getTotal());
                        return;
                }

                Double total = Double.valueOf(getTotal());
                Double cashRequired = Double.valueOf(cashReceiveTextField.getText());
                Double balance = cashRequired - total;
                balanceTextField.setText(String.valueOf(balance));
                printBill();
        }//GEN-LAST:event_payButtonActionPerformed

        private void taxTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_taxTextFieldKeyReleased
                setTotalAndSubTotalData(getSubTotal(), getTotal());
        }//GEN-LAST:event_taxTextFieldKeyReleased

        private void taxTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_taxTextFieldActionPerformed
                //        String subTotal = getSubTotal();
                //        subTotalTextField.setText(subTotal);
        }//GEN-LAST:event_taxTextFieldActionPerformed

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

        private void productsOrderTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productsOrderTableMouseClicked
		int selectedRowIndex = productsOrderTable.getSelectedRow();
		int productId = (int) productsOrderTable.getValueAt(selectedRowIndex, PRODUCT_ID_COLUMN_POSITION);
		Product product = productService.getProductById(productId);
		addToTable(product);
        }//GEN-LAST:event_productsOrderTableMouseClicked

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
        private javax.swing.JTextField balanceTextField;
        private javax.swing.JLabel bannerImageLabel;
        private javax.swing.JTextPane billTextPanel;
        private javax.swing.JTextField cashReceiveTextField;
        private javax.swing.JButton jButton1;
        private javax.swing.JButton jButton3;
        private javax.swing.JButton jButton4;
        private javax.swing.JButton jButton5;
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
        private javax.swing.JButton newOrderButton;
        private javax.swing.JButton payButton;
        private javax.swing.JTable productsOrderTable;
        private javax.swing.JTable productsTable;
        private javax.swing.JTextField subTotalTextField;
        private javax.swing.JTextField taxTextField;
        private javax.swing.JTextField totalTextField;
        // End of variables declaration//GEN-END:variables

}
