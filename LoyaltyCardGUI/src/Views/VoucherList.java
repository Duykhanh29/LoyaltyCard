/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Views;

import DAO.VoucherDao;
import Models.UserData;
import Models.Voucher;
import cache.VoucherCache;
import com.formdev.flatlaf.FlatLightLaf;
import constants.ComboBoxData;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.PopupMenu;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author datlogarit
 */
public class VoucherList extends javax.swing.JFrame {

    /**
     * Creates new form voucherPage
     */
    UserData userData;
    private static final String CACHE_LIST_ACTIVE_VOUCHER = "LIST_USER_VOUCHER";
    private static final long CACHE_EXPIRY_TIME = 5 * 60 * 1000;

    ComboBoxData comboBoxData = new ComboBoxData();
    Integer selectType = null;
    List<Voucher> listVoucher = new ArrayList<>();
    public VoucherList(UserData userData) {
        this.userData = userData;
        initComponents();
        this.setLocationRelativeTo(null);
        initViews();
    }

    private void initViews() {
        jPanel1.setLayout(new java.awt.GridLayout(0, 2, 10, 10));
        for (String value : comboBoxData.getKeyValueMap().values()) {
            jComboBox1.addItem(value);
        }
        jComboBox1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String selectedValue = (String) jComboBox1.getSelectedItem();
                Integer selectedKey = comboBoxData.getKeyByValue(selectedValue);
                selectType = selectedKey;
                List<Voucher> vouchers = filterByOption(listVoucher, selectType);
                displayListView(vouchers);
            }
        });
        init();
    }

    private void init() {
        try {
            jPanel1.removeAll();
            if (VoucherCache.getCache(CACHE_LIST_ACTIVE_VOUCHER) != null) {
                System.out.println("Cache existed");
                listVoucher = VoucherCache.getCache(CACHE_LIST_ACTIVE_VOUCHER);
            } else {
                System.out.println("Get voucher from DB");
                listVoucher = filterByType();
                if (listVoucher != null) {
                    VoucherCache.setCache(CACHE_LIST_ACTIVE_VOUCHER, listVoucher, CACHE_EXPIRY_TIME); // Lưu cache
                }
            }
            if (listVoucher == null) {
                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra");
                return;
            }
            displayListView(listVoucher);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra");
        }
    }

    private void displayListView(List<Voucher> listVoucher) {
        try {
            jPanel1.removeAll();
            for (Voucher voucher : listVoucher) {
                JPanel voucherPanel = createVoucherPanel(voucher);
                jPanel1.add(voucherPanel);
            }
            jPanel1.revalidate();
            jPanel1.repaint();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra");
        }
    }

    private List<Voucher> filterByType() {
        List<Voucher> listVoucher = new ArrayList<>();
        try {
            listVoucher = VoucherDao.getInstance().getVouchersByUserId(userData.getId(), selectType);   // set userId 35 for testing
        } catch (SQLException ex) {
            Logger.getLogger(VoucherList.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VoucherList.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listVoucher;
    }

    private List<Voucher> filterByOption(List<Voucher> listVoucher, Integer option) {
        List<Voucher> filteredList = new ArrayList<>();

        for (Voucher voucher : listVoucher) {
            if (option == 1) {
                filteredList.add(voucher); // Hiển thị tất cả
            } else if (option == 2 && voucher.getStatus() == 1
                    && !voucher.getStartTime().after(new java.sql.Date(System.currentTimeMillis()))
                    && !voucher.getEndTime().before(new java.sql.Date(System.currentTimeMillis()))) {
                filteredList.add(voucher); // Lọc active
            } else if (option == 3 && voucher.getStatus() == 0) {
                filteredList.add(voucher); // Lọc inactive
            }
        }

        return filteredList;
    }

    private JPanel createVoucherPanel(Voucher voucher) {
        // Panel chính với layout ngang
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout()); // Sử dụng BorderLayout để dễ căn chỉnh các thành phần
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 10));
        panel.setBackground(Color.WHITE);
        // Icon voucher
        JLabel lblImage = new JLabel(new javax.swing.ImageIcon(getClass().getResource("/Res/voucher_20K_60x60.png")));

        // Thông tin voucher: Tiêu đề, Điểm, Ngày
        JPanel textPanel = new JPanel();
        textPanel.setBackground(Color.WHITE);
        textPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        textPanel.setLayout(new javax.swing.BoxLayout(textPanel, javax.swing.BoxLayout.Y_AXIS)); // Layout dọc
        JLabel lblTitle = new JLabel(voucher.getName());
        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 12));
        JLabel lblPoint = new JLabel("Cần " + voucher.getPointsValue() + " điểm để quy đổi");
        JLabel lblDate = new JLabel("Date: " + voucher.getEndTime());

        // Thêm các phần tử vào textPanel
        textPanel.add(lblTitle);
        textPanel.add(lblPoint);
        textPanel.add(lblDate);

        // Gift icon và Detail panel (phía bên phải)
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS)); // Layout dọc
        rightPanel.setAlignmentX(Component.RIGHT_ALIGNMENT); // Đảm bảo nội dung trong rightPanel nằm bên phải
        rightPanel.setBackground(Color.WHITE);

        JLabel jlblGift = new JLabel();
        jlblGift.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/gitf_icon_50x50.jpg")));

        JLabel lblDetail = new JLabel("Detail");
        lblDetail.setFont(new java.awt.Font("Segoe UI", 1, 12));
        lblDetail.setForeground(new java.awt.Color(255, 51, 51));
        lblDetail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                moveDetail(evt, voucher); // Hành động khi nhấp chuột
            }
        });

        // Thêm icon gift và Detail vào rightPanel
        rightPanel.add(jlblGift); // Gift icon nằm trên
        rightPanel.add(lblDetail); // Detail nằm dưới

        // Đảm bảo khoảng cách đều cho các thành phần
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10)); // Khoảng cách từ các phần tử tới biên phải

        // Thêm các thành phần vào panel chính
        panel.add(lblImage, BorderLayout.WEST); // Thêm ảnh voucher vào bên trái
        panel.add(textPanel, BorderLayout.CENTER); // Thêm thông tin voucher vào giữa
        panel.add(rightPanel, BorderLayout.EAST); // Thêm gift icon và Detail vào bên phải

        // Thêm sự kiện click cho toàn bộ panel
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
            }
        });

        return panel;
    }

    private VoucherList() {
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jScrollPane1 = new scroll.win11.ScrollPaneWin11();
        jPanel1 = new javax.swing.JPanel();
        jLable_img_1K = new javax.swing.JLabel();
        jLabelTile_1K = new javax.swing.JLabel();
        jLabelPoint_1K = new javax.swing.JLabel();
        jLabelDate_1K = new javax.swing.JLabel();
        jLabelDetail_1K = new javax.swing.JLabel();
        jLabel_gift_1K = new javax.swing.JLabel();
        lbbg_1 = new javax.swing.JLabel();
        jLabelTile_1K1 = new javax.swing.JLabel();
        jLabelDate_1K1 = new javax.swing.JLabel();
        jLabel_gift_1K1 = new javax.swing.JLabel();
        jLable_img_1K1 = new javax.swing.JLabel();
        jLabelDetail_1K1 = new javax.swing.JLabel();
        jLabelPoint_1K1 = new javax.swing.JLabel();
        lbbg_2 = new javax.swing.JLabel();
        jLable_img_1K2 = new javax.swing.JLabel();
        jLabelTile_1K2 = new javax.swing.JLabel();
        jLabelPoint_1K2 = new javax.swing.JLabel();
        jLabelDate_1K2 = new javax.swing.JLabel();
        jLabelDetail_1K2 = new javax.swing.JLabel();
        jLabel_gift_1K2 = new javax.swing.JLabel();
        lbbg_3 = new javax.swing.JLabel();
        jLable_img_1K3 = new javax.swing.JLabel();
        jLabelTile_1K3 = new javax.swing.JLabel();
        jLabelPoint_1K3 = new javax.swing.JLabel();
        jLabelDate_1K3 = new javax.swing.JLabel();
        jLabelDetail_1K3 = new javax.swing.JLabel();
        jLabel_gift_1K3 = new javax.swing.JLabel();
        lbbg_4 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jbbackground = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Voucher");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 40, 227, 37));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Loại voucher");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 90, -1, -1));

        getContentPane().add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 90, -1, -1));

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLable_img_1K.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/voucher_20K_60x60.png"))); // NOI18N
        jPanel1.add(jLable_img_1K, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 60, 60));

        jLabelTile_1K.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelTile_1K.setText("VOUCHER GIẢM 20K");
        jPanel1.add(jLabelTile_1K, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 0, -1, -1));

        jLabelPoint_1K.setText("Cần 10.000 điểm để quy đổi");
        jPanel1.add(jLabelPoint_1K, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, -1, -1));

        jLabelDate_1K.setText("Date: 15/01/2025 ");
        jPanel1.add(jLabelDate_1K, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 40, -1, -1));

        jLabelDetail_1K.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelDetail_1K.setForeground(new java.awt.Color(255, 51, 51));
        jLabelDetail_1K.setText("Detail");
        jPanel1.add(jLabelDetail_1K, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 40, 40, -1));

        jLabel_gift_1K.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/gitf_icon_50x50.jpg"))); // NOI18N
        jPanel1.add(jLabel_gift_1K, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 0, 50, 40));

        lbbg_1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/white-background.png"))); // NOI18N
        lbbg_1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 4, 1));
        jPanel1.add(lbbg_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 290, 60));

        jLabelTile_1K1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelTile_1K1.setText("VOUCHER GIẢM 20K");
        jPanel1.add(jLabelTile_1K1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 70, -1, -1));

        jLabelDate_1K1.setText("Date: 15/01/2025 ");
        jPanel1.add(jLabelDate_1K1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 110, -1, -1));

        jLabel_gift_1K1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/gitf_icon_50x50.jpg"))); // NOI18N
        jPanel1.add(jLabel_gift_1K1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 70, -1, 40));

        jLable_img_1K1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/voucher_20K_60x60.png"))); // NOI18N
        jPanel1.add(jLable_img_1K1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, -1, 60));

        jLabelDetail_1K1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelDetail_1K1.setForeground(new java.awt.Color(255, 51, 51));
        jLabelDetail_1K1.setText("Detail");
        jPanel1.add(jLabelDetail_1K1, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 110, 40, -1));

        jLabelPoint_1K1.setText("Cần 10.000 điểm để quy đổi");
        jPanel1.add(jLabelPoint_1K1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 90, -1, -1));

        lbbg_2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/white-background.png"))); // NOI18N
        jPanel1.add(lbbg_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 290, 60));

        jLable_img_1K2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/voucher_20K_60x60.png"))); // NOI18N
        jPanel1.add(jLable_img_1K2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 60, 60));

        jLabelTile_1K2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelTile_1K2.setText("VOUCHER GIẢM 20K");
        jPanel1.add(jLabelTile_1K2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 140, -1, -1));

        jLabelPoint_1K2.setText("Cần 10.000 điểm để quy đổi");
        jPanel1.add(jLabelPoint_1K2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 160, -1, -1));

        jLabelDate_1K2.setText("Date: 15/01/2025 ");
        jPanel1.add(jLabelDate_1K2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 180, -1, -1));

        jLabelDetail_1K2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelDetail_1K2.setForeground(new java.awt.Color(255, 51, 51));
        jLabelDetail_1K2.setText("Detail");
        jPanel1.add(jLabelDetail_1K2, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 180, 40, -1));

        jLabel_gift_1K2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/gitf_icon_50x50.jpg"))); // NOI18N
        jPanel1.add(jLabel_gift_1K2, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 140, 50, 40));

        lbbg_3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/white-background.png"))); // NOI18N
        jPanel1.add(lbbg_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 290, 60));

        jLable_img_1K3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/voucher_20K_60x60.png"))); // NOI18N
        jPanel1.add(jLable_img_1K3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 210, 60, 60));

        jLabelTile_1K3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelTile_1K3.setText("VOUCHER GIẢM 20K");
        jPanel1.add(jLabelTile_1K3, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 210, -1, -1));

        jLabelPoint_1K3.setText("Cần 10.000 điểm để quy đổi");
        jPanel1.add(jLabelPoint_1K3, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 230, -1, -1));

        jLabelDate_1K3.setText("Date: 15/01/2025 ");
        jPanel1.add(jLabelDate_1K3, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 250, -1, -1));

        jLabelDetail_1K3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelDetail_1K3.setForeground(new java.awt.Color(255, 51, 51));
        jLabelDetail_1K3.setText("Detail");
        jLabelDetail_1K3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                moveDetail(evt);
            }
        });
        jPanel1.add(jLabelDetail_1K3, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 250, 40, -1));

        jLabel_gift_1K3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/gitf_icon_50x50.jpg"))); // NOI18N
        jPanel1.add(jLabel_gift_1K3, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 210, 50, 40));

        lbbg_4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/white-background.png"))); // NOI18N
        jPanel1.add(lbbg_4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 210, 290, 60));

        jScrollPane1.setViewportView(jPanel1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 130, 650, 280));

        jButton2.setBackground(new java.awt.Color(204, 204, 255));
        jButton2.setForeground(new java.awt.Color(0, 0, 51));
        jButton2.setText("Quay lại");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 90, -1));

        jButton1.setBackground(new java.awt.Color(206, 206, 255));
        jButton1.setText("Áp dụng");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 90, 90, -1));

        jbbackground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/background_900x600.jpg"))); // NOI18N
        getContentPane().add(jbbackground, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 760, 450));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        this.dispose();
        ExchangePoints exchangePoints = new ExchangePoints(userData);
        exchangePoints.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void moveDetail(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_moveDetail

        // TODO add your handling code here:
    }//GEN-LAST:event_moveDetail

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jButton1ActionPerformed

    private void moveDetail(java.awt.event.MouseEvent evt, Voucher voucher) {
        // TODO add your handling code here:
        DetailVoucher detailVoucher = new DetailVoucher(true, userData, voucher);
        this.dispose();
        detailVoucher.setVisible(true);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        FlatLightLaf.setup();
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VoucherList().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelDate_1K;
    private javax.swing.JLabel jLabelDate_1K1;
    private javax.swing.JLabel jLabelDate_1K2;
    private javax.swing.JLabel jLabelDate_1K3;
    private javax.swing.JLabel jLabelDetail_1K;
    private javax.swing.JLabel jLabelDetail_1K1;
    private javax.swing.JLabel jLabelDetail_1K2;
    private javax.swing.JLabel jLabelDetail_1K3;
    private javax.swing.JLabel jLabelPoint_1K;
    private javax.swing.JLabel jLabelPoint_1K1;
    private javax.swing.JLabel jLabelPoint_1K2;
    private javax.swing.JLabel jLabelPoint_1K3;
    private javax.swing.JLabel jLabelTile_1K;
    private javax.swing.JLabel jLabelTile_1K1;
    private javax.swing.JLabel jLabelTile_1K2;
    private javax.swing.JLabel jLabelTile_1K3;
    private javax.swing.JLabel jLabel_gift_1K;
    private javax.swing.JLabel jLabel_gift_1K1;
    private javax.swing.JLabel jLabel_gift_1K2;
    private javax.swing.JLabel jLabel_gift_1K3;
    private javax.swing.JLabel jLable_img_1K;
    private javax.swing.JLabel jLable_img_1K1;
    private javax.swing.JLabel jLable_img_1K2;
    private javax.swing.JLabel jLable_img_1K3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jbbackground;
    private javax.swing.JLabel lbbg_1;
    private javax.swing.JLabel lbbg_2;
    private javax.swing.JLabel lbbg_3;
    private javax.swing.JLabel lbbg_4;
    // End of variables declaration//GEN-END:variables
}
