/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Views;

import Controllers.PinController;
import Controllers.PointController;
import Controllers.RSAController;
import Controllers.SmartCardConnection;
import Controllers.UserDataController;
import DAO.PointTransactionDao;
import DAO.UserDao;
import DAO.UserVoucherDao;
import DAO.VoucherDao;
import Models.PointsTransaction;
import Models.UserData;
import Models.Voucher;
import cache.VoucherCache;
import com.formdev.flatlaf.FlatLightLaf;
import constants.AppletConstants;
import constants.Constants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

/**
 *
 * @author datlogarit
 */
public class ExchangePoints extends javax.swing.JFrame {

    /**
     * Creates new form ExchangePoints
     */
    UserData userData;
    PointController pointController;
    SmartCardConnection smartCardConnection;
    PinController pinController;
    UserDataController userDataController;
    private static final String CACHE_LIST_ACTIVE_VOUCHER = "LIST_ACTIVE_VOUCHER";
    private static final long CACHE_EXPIRY_TIME = 5 * 60 * 1000;

    public ExchangePoints(UserData userData) {
        initComponents();
        this.setLocationRelativeTo(null);
        this.userData = userData;
        initViews();
        smartCardConnection = SmartCardConnection.getInstance();
        pointController = new PointController(smartCardConnection);
        pinController = new PinController(smartCardConnection);
        userDataController = new UserDataController(smartCardConnection);
    }

    private ExchangePoints() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void initViews() {
        pointView.setText(String.valueOf(userData.getPoints()));
        jPanel1.setLayout(new java.awt.GridLayout(0, 2, 10, 10));
        init();
    }

    private void init() {
        noticeText.setText("* Chú ý: Khách hàng chỉ được chọn một trong các hạn mức trên");
//        noticeText1.setText("Số điểm không được đổi sang số tiền ");
        try {
            jPanel1.removeAll();
            List<Voucher> listVoucher = null;
            if (VoucherCache.getCache(CACHE_LIST_ACTIVE_VOUCHER) != null) {
                System.out.println("Cache existed");
                listVoucher = VoucherCache.getCache(CACHE_LIST_ACTIVE_VOUCHER);
            } else {
                System.out.println("Get voucher from DB");
                listVoucher = VoucherDao.getInstance().getActiveVouchers();
                if (listVoucher != null) {
                    VoucherCache.setCache(CACHE_LIST_ACTIVE_VOUCHER, listVoucher, CACHE_EXPIRY_TIME); // Lưu cache
                }
            }
            if (listVoucher == null) {
                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra");
                return;
            }
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
                chooseVoucher(evt, voucher); // Gọi hàm xử lý click
            }
        });

        return panel;
    }

    private void chooseVoucher(java.awt.event.MouseEvent evt, Voucher voucher) {
        // TODO add your handling code here:
        int point = voucher.getPointsValue();
        if (point > userData.getPoints()) {
            JOptionPane.showMessageDialog(this, "Bạn không đủ điểm để đổi voucher này", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int response = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn dùng " + point + " điểm để đổi phiếu giảm giá này ?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        if (response == JOptionPane.YES_OPTION) {
//                    JOptionPane.showMessageDialog(this, "Đổi điểm thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            exchangePoints(voucher);
        }
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
        jLabel3 = new javax.swing.JLabel();
        pointView = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        noticeText = new javax.swing.JLabel();
        jScrollPane1 = new scroll.win11.ScrollPaneWin11();
        jPanel1 = new javax.swing.JPanel();
        jLable_img_1K = new javax.swing.JLabel();
        jLabelTile_1K = new javax.swing.JLabel();
        jLabelPoint_1K = new javax.swing.JLabel();
        jLabelDate_1K = new javax.swing.JLabel();
        jLabelDetail_1K = new javax.swing.JLabel();
        jLabel_gift_1K = new javax.swing.JLabel();
        lbbg_1 = new javax.swing.JLabel();
        jLable_img_3 = new javax.swing.JLabel();
        jLabelTile_3 = new javax.swing.JLabel();
        jLabelPoint_3 = new javax.swing.JLabel();
        jLabelDate_3 = new javax.swing.JLabel();
        jLabelDetail_3 = new javax.swing.JLabel();
        jLabel_gift_3 = new javax.swing.JLabel();
        jLable_img_2K = new javax.swing.JLabel();
        jLabelTile_2K = new javax.swing.JLabel();
        jLabelPoint_2K = new javax.swing.JLabel();
        jLabelDate_2K = new javax.swing.JLabel();
        jLabelDetail_2K = new javax.swing.JLabel();
        jLabel_gift_2K = new javax.swing.JLabel();
        lbbg_2 = new javax.swing.JLabel();
        jLable_img_4 = new javax.swing.JLabel();
        jLabelTile_4 = new javax.swing.JLabel();
        jLabelPoint_4 = new javax.swing.JLabel();
        jLabelDate_4 = new javax.swing.JLabel();
        jLabelDetail_4 = new javax.swing.JLabel();
        jLabel_gift_4 = new javax.swing.JLabel();
        lbbg_4 = new javax.swing.JLabel();
        jLable_img_1K1 = new javax.swing.JLabel();
        jLabelTile_1K1 = new javax.swing.JLabel();
        jLabelPoint_1K1 = new javax.swing.JLabel();
        jLabelDate_1K1 = new javax.swing.JLabel();
        jLabelDetail_1K1 = new javax.swing.JLabel();
        jLabel_gift_1K1 = new javax.swing.JLabel();
        lbbg_3 = new javax.swing.JLabel();
        jLable_img_2K1 = new javax.swing.JLabel();
        jLabelTile_2K1 = new javax.swing.JLabel();
        jLabelPoint_2K1 = new javax.swing.JLabel();
        jLabelDate_2K1 = new javax.swing.JLabel();
        jLabelDetail_2K1 = new javax.swing.JLabel();
        jLabel_gift_2K1 = new javax.swing.JLabel();
        lbbg_5 = new javax.swing.JLabel();
        lbbg_6 = new javax.swing.JLabel();
        jbbackground = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("ĐỔI ĐIỂM TÍCH LŨY");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 50, -1, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Vui lòng chọn một trong các voucher sau");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 100, -1, -1));

        jLabel3.setBackground(new java.awt.Color(255, 0, 0));
        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel3.setText("Tổng điểm hiện có: ");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 390, -1, -1));

        pointView.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        pointView.setText("2002");
        getContentPane().add(pointView, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 390, -1, -1));

        jButton3.setBackground(new java.awt.Color(204, 204, 255));
        jButton3.setForeground(new java.awt.Color(0, 0, 51));
        jButton3.setText("Quay lại");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jButton4.setBackground(new java.awt.Color(204, 204, 255));
        jButton4.setForeground(new java.awt.Color(0, 0, 51));
        jButton4.setText("Voucher");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 0, 90, -1));

        noticeText.setForeground(new java.awt.Color(255, 51, 51));
        noticeText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(noticeText, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 350, 430, 24));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLable_img_1K.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/voucher_20K_60x60.png"))); // NOI18N
        jPanel1.add(jLable_img_1K, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        jLabelTile_1K.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelTile_1K.setText("VOUCHER GIẢM 20K");
        jPanel1.add(jLabelTile_1K, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 10, -1, -1));

        jLabelPoint_1K.setText("Cần 10.000 điểm để quy đổi");
        jPanel1.add(jLabelPoint_1K, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 30, -1, -1));

        jLabelDate_1K.setText("Date: 15/01/2025 ");
        jPanel1.add(jLabelDate_1K, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 50, -1, -1));

        jLabelDetail_1K.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelDetail_1K.setForeground(new java.awt.Color(255, 51, 51));
        jLabelDetail_1K.setText("Detail");
        jLabelDetail_1K.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                moveDetail(evt);
            }
        });
        jPanel1.add(jLabelDetail_1K, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 50, 40, -1));

        jLabel_gift_1K.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/gitf_icon_50x50.jpg"))); // NOI18N
        jPanel1.add(jLabel_gift_1K, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 10, -1, 40));

        lbbg_1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/white-background.png"))); // NOI18N
        lbbg_1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                chooseVoucher(evt);
            }
        });
        jPanel1.add(lbbg_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 290, 60));

        jLable_img_3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/voucher_100K_60x60.png"))); // NOI18N
        jPanel1.add(jLable_img_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        jLabelTile_3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelTile_3.setText("VOUCHER GIẢM 100K");
        jPanel1.add(jLabelTile_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 90, -1, -1));

        jLabelPoint_3.setText("Cần 10.000 điểm để quy đổi");
        jPanel1.add(jLabelPoint_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 110, -1, -1));

        jLabelDate_3.setText("Date: 15/01/2025 ");
        jPanel1.add(jLabelDate_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 130, -1, -1));

        jLabelDetail_3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelDetail_3.setForeground(new java.awt.Color(255, 51, 51));
        jLabelDetail_3.setText("Detail");
        jLabelDetail_3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                moveDetail(evt);
            }
        });
        jPanel1.add(jLabelDetail_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 130, 40, -1));

        jLabel_gift_3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/gitf_icon_50x50.jpg"))); // NOI18N
        jPanel1.add(jLabel_gift_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 90, -1, 40));

        jLable_img_2K.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/voucher_50K_60x60.png"))); // NOI18N
        jPanel1.add(jLable_img_2K, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 10, -1, -1));

        jLabelTile_2K.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelTile_2K.setText("VOUCHER GIẢM 50K");
        jPanel1.add(jLabelTile_2K, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 10, -1, -1));

        jLabelPoint_2K.setText("Cần 10.000 điểm để quy đổi");
        jPanel1.add(jLabelPoint_2K, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 30, -1, -1));

        jLabelDate_2K.setText("Date: 15/01/2025 ");
        jPanel1.add(jLabelDate_2K, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 50, -1, -1));

        jLabelDetail_2K.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelDetail_2K.setForeground(new java.awt.Color(255, 51, 51));
        jLabelDetail_2K.setText("Detail");
        jLabelDetail_2K.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                moveDetail(evt);
            }
        });
        jPanel1.add(jLabelDetail_2K, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 50, 40, -1));

        jLabel_gift_2K.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/gitf_icon_50x50.jpg"))); // NOI18N
        jPanel1.add(jLabel_gift_2K, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 10, -1, 40));

        lbbg_2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/white-background.png"))); // NOI18N
        lbbg_2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                chooseVoucher(evt);
            }
        });
        jPanel1.add(lbbg_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 10, 290, 60));

        jLable_img_4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/voucher_200K_60x60.png"))); // NOI18N
        jPanel1.add(jLable_img_4, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 90, -1, -1));

        jLabelTile_4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelTile_4.setText("VOUCHER GIẢM 200K");
        jPanel1.add(jLabelTile_4, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 90, -1, -1));

        jLabelPoint_4.setText("Cần 10.000 điểm để quy đổi");
        jPanel1.add(jLabelPoint_4, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 110, -1, -1));

        jLabelDate_4.setText("Date: 15/01/2025 ");
        jPanel1.add(jLabelDate_4, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 130, -1, -1));

        jLabelDetail_4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelDetail_4.setForeground(new java.awt.Color(255, 51, 51));
        jLabelDetail_4.setText("Detail");
        jLabelDetail_4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                moveDetail(evt);
            }
        });
        jPanel1.add(jLabelDetail_4, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 130, 40, -1));

        jLabel_gift_4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/gitf_icon_50x50.jpg"))); // NOI18N
        jPanel1.add(jLabel_gift_4, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 90, -1, 40));

        lbbg_4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/white-background.png"))); // NOI18N
        lbbg_4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                chooseVoucher(evt);
            }
        });
        jPanel1.add(lbbg_4, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 90, 290, 60));

        jLable_img_1K1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/voucher_20K_60x60.png"))); // NOI18N
        jPanel1.add(jLable_img_1K1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, -1, -1));

        jLabelTile_1K1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelTile_1K1.setText("VOUCHER GIẢM 20K");
        jPanel1.add(jLabelTile_1K1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 170, -1, -1));

        jLabelPoint_1K1.setText("Cần 10.000 điểm để quy đổi");
        jPanel1.add(jLabelPoint_1K1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 190, -1, -1));

        jLabelDate_1K1.setText("Date: 15/01/2025 ");
        jPanel1.add(jLabelDate_1K1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 210, -1, -1));

        jLabelDetail_1K1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelDetail_1K1.setForeground(new java.awt.Color(255, 51, 51));
        jLabelDetail_1K1.setText("Detail");
        jLabelDetail_1K1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                moveDetail(evt);
            }
        });
        jPanel1.add(jLabelDetail_1K1, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 210, 40, -1));

        jLabel_gift_1K1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/gitf_icon_50x50.jpg"))); // NOI18N
        jPanel1.add(jLabel_gift_1K1, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 170, -1, 40));

        lbbg_3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/white-background.png"))); // NOI18N
        lbbg_3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                chooseVoucher(evt);
            }
        });
        jPanel1.add(lbbg_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 290, 60));

        jLable_img_2K1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/voucher_50K_60x60.png"))); // NOI18N
        jPanel1.add(jLable_img_2K1, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 170, -1, -1));

        jLabelTile_2K1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelTile_2K1.setText("VOUCHER GIẢM 50K");
        jPanel1.add(jLabelTile_2K1, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 170, -1, -1));

        jLabelPoint_2K1.setText("Cần 10.000 điểm để quy đổi");
        jPanel1.add(jLabelPoint_2K1, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 190, -1, -1));

        jLabelDate_2K1.setText("Date: 15/01/2025 ");
        jPanel1.add(jLabelDate_2K1, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 210, -1, -1));

        jLabelDetail_2K1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelDetail_2K1.setForeground(new java.awt.Color(255, 51, 51));
        jLabelDetail_2K1.setText("Detail");
        jLabelDetail_2K1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                moveDetail(evt);
            }
        });
        jPanel1.add(jLabelDetail_2K1, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 210, 40, -1));

        jLabel_gift_2K1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/gitf_icon_50x50.jpg"))); // NOI18N
        jPanel1.add(jLabel_gift_2K1, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 170, -1, 40));

        lbbg_5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/white-background.png"))); // NOI18N
        lbbg_5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                chooseVoucher(evt);
            }
        });
        jPanel1.add(lbbg_5, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 170, 290, 60));

        lbbg_6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/white-background.png"))); // NOI18N
        lbbg_6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                chooseVoucher(evt);
            }
        });
        jPanel1.add(lbbg_6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 290, 60));

        jScrollPane1.setViewportView(jPanel1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 140, 700, 200));

        jbbackground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Res/background_800x533.jpg"))); // NOI18N
        getContentPane().add(jbbackground, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 460));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        this.dispose();
        HomeView homeView = new HomeView();
        homeView.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void exchangePoints(Voucher voucher) {
        try {
            JFrame frame = new JFrame("Nhập mã PIN");
            JPasswordField passwordField = new JPasswordField(10);
            int option = JOptionPane.showConfirmDialog(frame, passwordField, "Nhập mã PIN", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (option == JOptionPane.OK_OPTION) {
                char[] pin = passwordField.getPassword();
                String pinStr = new String(pin);
                System.out.println("Mã PIN bạn nhập là: " + pinStr);
                onHandleExchangePoint(pinStr, voucher);
            }
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Điểm không hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } finally {
        }
    }

    private void onHandleExchangePoint(String pin, Voucher voucher) {
        if (pin != null && !pin.isEmpty()) {
            try {
                int pinTries = pinController.verifyPin(pin);
                if (pinTries == AppletConstants.VERIFY_SUCCESS) {
                    RSAController rsaController = new RSAController(userDataController);
                    boolean isVerifyRSA = rsaController.verifyRSA(this, userData.getPublicKey());
                    if (!isVerifyRSA) {
                        JOptionPane.showMessageDialog(this, "Xác thực RSA thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                    UserVoucherDao.getInstance().insertUserVoucher(userData.getId(), voucher.getId(), 1);
                    userData.setPoints((short) (userData.getPoints() - voucher.getPointsValue()));
                    UserDao.getInstance().updateUser(userData);
                    insertTrans(voucher);
                    boolean isSucess = pointController.updatePoint((short) voucher.getPointsValue(), false);
                    if (isSucess) {
                        JOptionPane.showMessageDialog(this, "Đổi điểm thành công", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        onBackToHomeView();
                    } else {
                        JOptionPane.showMessageDialog(this, "Đổi điểm không thành công", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Mã PIN sai. Vui lòng thử lại. Bạn còn " + pinTries + " lần", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra. Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Mã PIN không thể trống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void insertTrans(Voucher voucher) throws ClassNotFoundException {
        PointsTransaction pointsTransaction = new PointsTransaction();
        pointsTransaction.setPoints(voucher.getPointsValue());
        pointsTransaction.setResourceId(voucher.getId());
        pointsTransaction.setTransactionType(Constants.TRANSACTION_TYPE.SUBTRACT);
        pointsTransaction.setUserId(userData.getId());
        pointsTransaction.setDescription("Trừ điểm do đổi voucher.");
        PointTransactionDao.getInstance().insertPointTransaction(pointsTransaction);
    }

    private void onBackToHomeView() {
        HomeView homeView = new HomeView(userData);
        this.dispose();
        homeView.setVisible(true);
    }

    private void moveDetail(java.awt.event.MouseEvent evt, Voucher voucher) {
        // TODO add your handling code here:
        DetailVoucher detailVoucher = new DetailVoucher(false, userData, voucher);
        this.dispose();
        detailVoucher.setVisible(true);
    }
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        HomeView homeView = new HomeView(userData);
        this.dispose();
        homeView.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        this.dispose();
        VoucherList vouchePage = new VoucherList(userData);
        vouchePage.setVisible(true);
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void moveDetail(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_moveDetail
        // TODO add your handling code here:
    }//GEN-LAST:event_moveDetail

    private void chooseVoucher(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_chooseVoucher
        // TODO add your handling code here:
        int point = 10;
        int response = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn dùng " + point + " điểm để đổi phiếu giảm giá này ?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        if (response == JOptionPane.YES_OPTION) {
//                    JOptionPane.showMessageDialog(this, "Đổi điểm thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//            exchangePoints(vou);
        }
    }//GEN-LAST:event_chooseVoucher

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        FlatLightLaf.setup();
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ExchangePoints().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelDate_1K;
    private javax.swing.JLabel jLabelDate_1K1;
    private javax.swing.JLabel jLabelDate_2K;
    private javax.swing.JLabel jLabelDate_2K1;
    private javax.swing.JLabel jLabelDate_3;
    private javax.swing.JLabel jLabelDate_4;
    private javax.swing.JLabel jLabelDetail_1K;
    private javax.swing.JLabel jLabelDetail_1K1;
    private javax.swing.JLabel jLabelDetail_2K;
    private javax.swing.JLabel jLabelDetail_2K1;
    private javax.swing.JLabel jLabelDetail_3;
    private javax.swing.JLabel jLabelDetail_4;
    private javax.swing.JLabel jLabelPoint_1K;
    private javax.swing.JLabel jLabelPoint_1K1;
    private javax.swing.JLabel jLabelPoint_2K;
    private javax.swing.JLabel jLabelPoint_2K1;
    private javax.swing.JLabel jLabelPoint_3;
    private javax.swing.JLabel jLabelPoint_4;
    private javax.swing.JLabel jLabelTile_1K;
    private javax.swing.JLabel jLabelTile_1K1;
    private javax.swing.JLabel jLabelTile_2K;
    private javax.swing.JLabel jLabelTile_2K1;
    private javax.swing.JLabel jLabelTile_3;
    private javax.swing.JLabel jLabelTile_4;
    private javax.swing.JLabel jLabel_gift_1K;
    private javax.swing.JLabel jLabel_gift_1K1;
    private javax.swing.JLabel jLabel_gift_2K;
    private javax.swing.JLabel jLabel_gift_2K1;
    private javax.swing.JLabel jLabel_gift_3;
    private javax.swing.JLabel jLabel_gift_4;
    private javax.swing.JLabel jLable_img_1K;
    private javax.swing.JLabel jLable_img_1K1;
    private javax.swing.JLabel jLable_img_2K;
    private javax.swing.JLabel jLable_img_2K1;
    private javax.swing.JLabel jLable_img_3;
    private javax.swing.JLabel jLable_img_4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jbbackground;
    private javax.swing.JLabel lbbg_1;
    private javax.swing.JLabel lbbg_2;
    private javax.swing.JLabel lbbg_3;
    private javax.swing.JLabel lbbg_4;
    private javax.swing.JLabel lbbg_5;
    private javax.swing.JLabel lbbg_6;
    private javax.swing.JLabel noticeText;
    private javax.swing.JLabel pointView;
    // End of variables declaration//GEN-END:variables
}
