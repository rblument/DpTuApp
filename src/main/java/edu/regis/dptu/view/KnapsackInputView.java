/*
 * DPTu: Dynamic Programming Tutor
 *
 *  (C) Johanna & Richard Blumenthal, All rights reserved
 *  ...
 */
package edu.regis.dptu.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.regis.dptu.model.KnapsackProblem;
import edu.regis.dptu.model.ProblemListener;
import edu.regis.dptu.util.ResourceMgr;

/**
 * KnapsackInputView lets the user specify a list of items (name, weight, value) and a knapsack
 * capacity, then submit them as a {@link KnapsackProblem}.
 *
 * <p>Follows the same pattern as MatrixInputView and LCSInputView.
 *
 * @author Cormac
 */
public class KnapsackInputView extends JPanel {

    private static final Logger log = LoggerFactory.getLogger(KnapsackInputView.class);

    private final ProblemListener submitListener;

    private final JTextField nameField = new JTextField(10);
    private final JTextField weightField = new JTextField(6);
    private final JTextField valueField = new JTextField(6);
    private final JTextField capacityField = new JTextField(6);

    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> itemList = new JList<>(listModel);

    private final List<String> itemNames = new ArrayList<>();
    private final List<Integer> itemWeights = new ArrayList<>();
    private final List<Integer> itemValues = new ArrayList<>();

    public KnapsackInputView() {
        this(null);
    }

    public KnapsackInputView(ProblemListener listener) {
        this.submitListener = listener;
        log.debug("Initializing KnapsackInputView");

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.anchor = GridBagConstraints.WEST;
        int row = 0;

        // Column headers
        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel(ResourceMgr.instance().string("knapsackInput.label.name")), gbc);
        gbc.gridx = 1;
        add(new JLabel(ResourceMgr.instance().string("knapsackInput.label.weight")), gbc);
        gbc.gridx = 2;
        add(new JLabel(ResourceMgr.instance().string("knapsackInput.label.value")), gbc);
        row++;

        // Entry fields + Add button
        gbc.gridx = 0;
        gbc.gridy = row;
        add(nameField, gbc);
        gbc.gridx = 1;
        add(weightField, gbc);
        gbc.gridx = 2;
        add(valueField, gbc);
        JButton addButton =
                new JButton(ResourceMgr.instance().string("knapsackInput.button.addItem"));
        gbc.gridx = 3;
        add(addButton, gbc);
        row++;

        // Remove button
        JButton removeButton =
                new JButton(ResourceMgr.instance().string("knapsackInput.button.removeItem"));
        gbc.gridx = 0;
        gbc.gridy = row;
        add(removeButton, gbc);
        row++;

        // Scrollable item list
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        add(new JScrollPane(itemList), gbc);
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        row++;

        // Capacity
        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel(ResourceMgr.instance().string("knapsackInput.label.capacity")), gbc);
        gbc.gridx = 1;
        add(capacityField, gbc);
        row++;

        // Submit button
        JButton submitButton =
                new JButton(ResourceMgr.instance().string("knapsackInput.button.submit"));
        gbc.gridx = 3;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.EAST;
        add(submitButton, gbc);

        addButton.addActionListener(e -> handleAddItem());
        removeButton.addActionListener(e -> handleRemoveSelected());
        submitButton.addActionListener(e -> handleSubmit());
    }

    private void handleAddItem() {
        String name = nameField.getText().trim();
        String wTxt = weightField.getText().trim();
        String vTxt = valueField.getText().trim();

        if (name.isEmpty() || wTxt.isEmpty() || vTxt.isEmpty()) {
            showError(ResourceMgr.instance().string("knapsackInput.error.emptyItemField"));
            return;
        }
        try {
            int w = Integer.parseInt(wTxt);
            int v = Integer.parseInt(vTxt);
            if (w <= 0 || v <= 0) {
                showError(ResourceMgr.instance().string("knapsackInput.error.positiveNumbers"));
                return;
            }
            itemNames.add(name);
            itemWeights.add(w);
            itemValues.add(v);
            listModel.addElement(String.format("%s  (w=%d, v=%d)", name, w, v));
            nameField.setText("");
            weightField.setText("");
            valueField.setText("");
            log.debug("Added item: {} w={} v={}", name, w, v);
        } catch (NumberFormatException ex) {
            showError(ResourceMgr.instance().string("knapsackInput.error.notInteger"));
        }
    }

    private void handleRemoveSelected() {
        int idx = itemList.getSelectedIndex();
        if (idx >= 0 && idx < itemNames.size()) {
            itemNames.remove(idx);
            itemWeights.remove(idx);
            itemValues.remove(idx);
            listModel.remove(idx);
        }
    }

    private void handleSubmit() {
        if (itemNames.isEmpty()) {
            showError(ResourceMgr.instance().string("knapsackInput.error.noItems"));
            return;
        }
        String capTxt = capacityField.getText().trim();
        if (capTxt.isEmpty()) {
            showError(ResourceMgr.instance().string("knapsackInput.error.noCapacity"));
            return;
        }
        int capacity;
        try {
            capacity = Integer.parseInt(capTxt);
            if (capacity <= 0) {
                showError(ResourceMgr.instance().string("knapsackInput.error.positiveCapacity"));
                return;
            }
        } catch (NumberFormatException ex) {
            showError(ResourceMgr.instance().string("knapsackInput.error.notInteger"));
            return;
        }

        String[] names = itemNames.toArray(new String[0]);
        int[] weights = itemWeights.stream().mapToInt(Integer::intValue).toArray();
        int[] values = itemValues.stream().mapToInt(Integer::intValue).toArray();
        KnapsackProblem problem = new KnapsackProblem(names, weights, values, capacity);

        if (submitListener != null) {
            submitListener.problemUpdated(problem);
            log.debug("KnapsackProblem submitted: n={}, W={}", names.length, capacity);
        } else {
            log.warn("KnapsackInputView: submitListener is null — problem not dispatched");
        }
    }

    /**
     * Pre-populate the view from an existing KnapsackProblem (e.g. loaded from the DB). Called by
     * ProblemInputView.setModel().
     */
    public void setDefaultItems(String[] names, int[] weights, int[] values, int capacity) {
        clearAll();
        for (int i = 0; i < names.length; i++) {
            itemNames.add(names[i]);
            itemWeights.add(weights[i]);
            itemValues.add(values[i]);
            listModel.addElement(
                    String.format("%s  (w=%d, v=%d)", names[i], weights[i], values[i]));
        }
        capacityField.setText(String.valueOf(capacity));
        log.debug("setDefaultItems: {} items, W={}", names.length, capacity);
    }

    public void clearAll() {
        itemNames.clear();
        itemWeights.clear();
        itemValues.clear();
        listModel.clear();
        nameField.setText("");
        weightField.setText("");
        valueField.setText("");
        capacityField.setText("");
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                ResourceMgr.instance().string("knapsackInput.error.title"),
                JOptionPane.ERROR_MESSAGE);
    }

    // Accessors for testing
    public List<String> getItemNames() {
        return new ArrayList<>(itemNames);
    }

    public List<Integer> getItemWeights() {
        return new ArrayList<>(itemWeights);
    }

    public List<Integer> getItemValues() {
        return new ArrayList<>(itemValues);
    }
}
