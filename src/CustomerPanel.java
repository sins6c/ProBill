
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class CustomerPanel extends JPanel {
    private JTextField txtName;
    private JTextField txtPhone;
    private JTextField txtEmail;
    private JTable table;
    private DefaultTableModel model;
    private String selectedId = null;

    /**
     * Create the panel.
     */
    public CustomerPanel() {
        setLayout(null);
        setBackground(Color.WHITE); // Default, will be handled by Theme
        setBounds(0, 0, 840, 619);

        JLabel lblHeading = new JLabel("Manage Customers");
        lblHeading.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblHeading.setBounds(300, 20, 250, 30);
        add(lblHeading);

        JLabel lblName = new JLabel("Name:");
        lblName.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblName.setBounds(50, 80, 80, 20);
        add(lblName);

        txtName = new JTextField();
        txtName.setBounds(120, 80, 150, 25);
        add(txtName);
        txtName.setColumns(10);

        JLabel lblPhone = new JLabel("Phone:");
        lblPhone.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblPhone.setBounds(300, 80, 80, 20);
        add(lblPhone);

        txtPhone = new JTextField();
        txtPhone.setBounds(360, 80, 150, 25);
        add(txtPhone);
        txtPhone.setColumns(10);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblEmail.setBounds(550, 80, 80, 20);
        add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(610, 80, 150, 25);
        add(txtEmail);
        txtEmail.setColumns(10);

        JButton btnAdd = new JButton("Add");
        btnAdd.setBounds(150, 130, 80, 30);
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = txtName.getText();
                String phone = txtPhone.getText();
                String email = txtEmail.getText();

                if (name.isEmpty() || phone.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Name and Phone are required!");
                    return;
                }

                if (DB.checkCustomerPhone(phone)) {
                    JOptionPane.showMessageDialog(null, "Customer with this phone already exists!");
                    return;
                }

                DB.addCustomer(name, phone, email);
                refreshTable();
                clearFields();
            }
        });
        add(btnAdd);

        JButton btnUpdate = new JButton("Update");
        btnUpdate.setBounds(250, 130, 80, 30);
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectedId == null) {
                    JOptionPane.showMessageDialog(null, "Please select a customer to update!");
                    return;
                }
                String name = txtName.getText();
                String phone = txtPhone.getText();
                String email = txtEmail.getText();

                DB.updateCustomer(selectedId, name, phone, email);
                refreshTable();
                clearFields();
                selectedId = null;
            }
        });
        add(btnUpdate);

        JButton btnDelete = new JButton("Delete");
        btnDelete.setBounds(350, 130, 80, 30);
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectedId == null) {
                    JOptionPane.showMessageDialog(null, "Please select a customer to delete!");
                    return;
                }
                int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this customer?",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    DB.deleteCustomer(selectedId);
                    refreshTable();
                    clearFields();
                    selectedId = null;
                }
            }
        });
        add(btnDelete);

        JButton btnClear = new JButton("Clear");
        btnClear.setBounds(450, 130, 80, 30);
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearFields();
                selectedId = null;
            }
        });
        add(btnClear);

        // Table to show customers
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(30, 180, 760, 350);
        add(scrollPane);

        table = new JTable();
        model = new DefaultTableModel(new Object[][] {},
                new String[] { "ID", "Name", "Phone", "Email", "Loyalty Points" }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(model);
        scrollPane.setViewportView(table);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    selectedId = model.getValueAt(row, 0).toString();
                    txtName.setText(model.getValueAt(row, 1).toString());
                    txtPhone.setText(model.getValueAt(row, 2).toString());
                    txtEmail.setText(model.getValueAt(row, 3).toString());
                }
            }
        });

        refreshTable();
    }

    public void refreshTable() {
        model.setRowCount(0);
        ArrayList<String[]> list = DB.getCustomers();
        for (String[] row : list) {
            model.addRow(row);
        }
    }

    private void clearFields() {
        txtName.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
    }
}
