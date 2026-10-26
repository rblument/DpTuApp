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

/*
 * Currently, KnapsackInputView is thought to be designed as a Single Input field with an Add button
 * to then append it to a list with all inputs, that can then be passed to a display variable in another View,
 * which can be used and visualized there. NOT IMPLEMENTED OR TESTED, ONLY TEMPLATED.
 *
 * Updated 10/24/26: Template replace with view implmenation KnapsackInputView.
 * Items name, weight, value can now be entered and stored.
 * View is patterned after the LCSInputView and MatrixInputView.
 */
public class KnapsackInputView extends JPanel {
    private static final Logger log = LoggerFactory.getLogger(KnapsackInputView.class);

    private final ProblemListener submitListener;

    private final JTextField nameField = new JTextField(12);
    private final JTextField weightField = new JTextField(5);
    private final JTextField valueField = new JTextField(5);
    private final JTextField capacityField = new JTextField(5);

    // Backing data, index-aligned with itemListModel.
    private final List<String> names = new ArrayList<>();
    private final List<Integer> weights = new ArrayList<>();
    private final List<Integer> values = new ArrayList<>();

    private final DefaultListModel<String> itemListModel = new DefaultListModel<>();
    private final JList<String> itemList = new JList<>(itemListModel);

    public KnapsackInputView(ProblemListener listener) {
        submitListener = listener;
        log.debug("Initializing KnapsackInputView");

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel(ResourceMgr.instance().string("knapsackInput.label.name")), gbc);
        gbc.gridx = 1;
        add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel(ResourceMgr.instance().string("knapsackInput.label.weight")), gbc);
        gbc.gridx = 1;
        add(weightField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel(ResourceMgr.instance().string("knapsackInput.label.value")), gbc);
        gbc.gridx = 1;
        add(valueField, gbc);

        JButton addButton =
                new JButton(ResourceMgr.instance().string("knapsackInput.button.addItem"));
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        add(addButton, gbc);

        JButton removeButton =
                new JButton(ResourceMgr.instance().string("knapsackInput.button.removeSelected"));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        add(removeButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        add(new JScrollPane(itemList), gbc);

        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel(ResourceMgr.instance().string("knapsackInput.label.capacity")), gbc);
        gbc.gridx = 1;
        add(capacityField, gbc);

        JButton submitButton =
                new JButton(ResourceMgr.instance().string("knapsackInput.button.submit"));
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.EAST;
        add(submitButton, gbc);

        addButton.addActionListener(e -> handleAddItem());
        removeButton.addActionListener(e -> handleRemoveSelected());

        // Listener to process inputs when Submit is clicked
        submitButton.addActionListener(e -> submitInputs());
    }

    /** Validate item and add to the list. */
    private void handleAddItem() {
        String name = nameField.getText().trim();
        String weightText = weightField.getText().trim();
        String valueText = valueField.getText().trim();

        if (name.isEmpty() || weightText.isEmpty() || valueText.isEmpty()) {
            return; // nothing to add
        }

        try {
            int weight = Integer.parseInt(weightText);
            int value = Integer.parseInt(valueText);

            if (weight <= 0 || value <= 0) {
                showValidationError(
                        ResourceMgr.instance().string("knapsackInput.error.positiveWhole"));
                return;
            }

            addItem(name, weight, value);

            nameField.setText("");
            weightField.setText("");
            valueField.setText("");

            log.debug("Added knapsack item: {} (w={}, v={})", name, weight, value);
        } catch (NumberFormatException ex) {
            log.warn("Invalid knapsack item input: weight={}, value={}", weightText, valueText, ex);
            showValidationError(ResourceMgr.instance().string("knapsackInput.error.positiveWhole"));
        }
    }

    private void addItem(String name, int weight, int value) {
        names.add(name);
        weights.add(weight);
        values.add(value);
        itemListModel.addElement(name + " (w=" + weight + ", v=" + value + ")");
    }

    // Clear selected item from all lists.
    private void handleRemoveSelected() {
        int index = itemList.getSelectedIndex();
        if (index >= 0 && index < names.size()) {
            names.remove(index);
            weights.remove(index);
            values.remove(index);
            itemListModel.remove(index);
            log.debug("Removed knapsack item at index {}", index);
        }
    }

    // build a KnapsackProblem and notify the listener
    public void submitInputs() {
        if (names.isEmpty()) {
            showValidationError(ResourceMgr.instance().string("knapsackInput.error.noItems"));
            return;
        }

        int capacity;
        try {
            capacity = Integer.parseInt(capacityField.getText().trim());
        } catch (NumberFormatException ex) {
            showValidationError(ResourceMgr.instance().string("knapsackInput.error.capacity"));
            return;
        }

        if (capacity <= 0) {
            showValidationError(ResourceMgr.instance().string("knapsackInput.error.capacity"));
            return;
        }

        String[] nameArr = names.toArray(new String[0]);
        int[] weightArr = weights.stream().mapToInt(Integer::intValue).toArray();
        int[] valueArr = values.stream().mapToInt(Integer::intValue).toArray();

        KnapsackProblem problem = new KnapsackProblem(nameArr, weightArr, valueArr, capacity);
        submitListener.problemUpdated(problem);
    }

    // populate view from the loaded model;
    public void setDefaults(KnapsackProblem problem) {
        clearAll();

        String[] defaultNames = problem.getNames();
        int[] defaultWeights = problem.getWeights();
        int[] defaultValues = problem.getValues();
        for (int i = 0; i < defaultNames.length; i++) {
            addItem(defaultNames[i], defaultWeights[i], defaultValues[i]);
        }

        capacityField.setText(String.valueOf(problem.getCapacity()));
    }

    // Reset view
    public void clearAll() {
        names.clear();
        weights.clear();
        values.clear();
        itemListModel.clear();
        nameField.setText("");
        weightField.setText("");
        valueField.setText("");
        capacityField.setText("");
    }

    private void showValidationError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                ResourceMgr.instance().string("dialog.title.invalidKnapsackInput"),
                JOptionPane.WARNING_MESSAGE);
    }
}
