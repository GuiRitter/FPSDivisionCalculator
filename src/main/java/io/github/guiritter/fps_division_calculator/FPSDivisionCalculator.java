package io.github.guiritter.fps_division_calculator;

import static io.github.guiritter.graphical_user_interface.LabelledComponentFactory.buildLabelledComponent;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class FPSDivisionCalculator {

	private static final int HALF_PADDING = 5;

	private static final int FULL_PADDING = 2 * HALF_PADDING;

	private static final String BLENDER = "Blender";

	private static final String KDENLIVE = "Kdenlive";

	private static final String typeArray[] = new String[]{BLENDER, KDENLIVE};

	static {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
	}

	private static final GridBagConstraints buildGBC(
			int y,
			int topPadding,
			int bottomPadding,
			int fill,
			Double weighty
	) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = y;
		gbc.fill = fill;
		gbc.weightx = 1.0;
		if (weighty != null) {
			gbc.weighty = weighty;
		}
		gbc.insets = new Insets(topPadding, FULL_PADDING, bottomPadding, FULL_PADDING);
		return gbc;
	}

	public static void main(String args[]) {
		JFrame frame = new JFrame("FPS Division Calculator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridBagLayout());

		int y = 0;
		JTextArea resultArea = new JTextArea();
		JProgressBar progressBar = new JProgressBar();

		JComboBox<String> comboBox = new JComboBox<>();
		comboBox.setModel(new DefaultComboBoxModel<String>(typeArray));

		frame.getContentPane().add(buildLabelledComponent(
				"Type",
				comboBox,
				SwingConstants.CENTER,
				SwingConstants.LEFT,
				0
		), buildGBC(
				y++,
				FULL_PADDING,
				HALF_PADDING,
				GridBagConstraints.HORIZONTAL,
				null
		));

		JTextField targetField = new JTextField("30");

		frame.getContentPane().add(buildLabelledComponent(
				"Target FPS",
				targetField,
				SwingConstants.CENTER,
				SwingConstants.LEFT,
				0
		), buildGBC(
				y++,
				HALF_PADDING,
				HALF_PADDING,
				GridBagConstraints.HORIZONTAL,
				null
		));

		JButton computeButton = new JButton("Compute");
		computeButton.addActionListener((ActionEvent event) -> {
			BigDecimal target;
			try {
				String text;
				text = targetField.getText();
				if ((text == null) || text.trim().isEmpty()) {
					return;
				}
				target = new BigDecimal(text);
			} catch (Throwable ex) {
				return;
			}
			resultArea.setText("");
			if (BLENDER.equals(comboBox.getModel().getSelectedItem())) {
				JOptionPane.showMessageDialog(
						frame,
						"Not implemented yet.",
						"Warning",
						JOptionPane.WARNING_MESSAGE
				);
				return;
			}
			if (KDENLIVE.equals(comboBox.getModel().getSelectedItem())) {
				(new Thread(new Runnable(){
				
					@Override
					public void run() {
						double quotient;
						double distanceToTarget;
						int minDividend = 0;
						int minDivisor = 0;
						double minDistanceToTarget = Double.POSITIVE_INFINITY;
						for (int dividend = 0; dividend < 500001; dividend++) {
							for (int divisor = 0; divisor < 10001; divisor++) {
								quotient = ((double) dividend) / ((double) divisor);
								distanceToTarget = Math.abs(target.doubleValue() - quotient);
								if (minDistanceToTarget > distanceToTarget) {
									minDistanceToTarget = distanceToTarget;
									minDividend = dividend;
									minDivisor = divisor;
								}
							}
							if ((dividend % 5000) == 0) {
								progressBar.setValue(dividend / 5000);
							}
						}
						resultArea.setText(minDividend + "/" + minDivisor + "≈" + target);
					}
				})).start();
				return;
			}
			JOptionPane.showMessageDialog(
					frame,
					"Invalid option selected.",
					"Errro",
					JOptionPane.ERROR_MESSAGE
			);
		});

		frame.getContentPane().add(computeButton, buildGBC(
				y++,
				HALF_PADDING,
				HALF_PADDING,
				GridBagConstraints.HORIZONTAL,
				null
		));

		progressBar.setStringPainted(true);
		progressBar.setValue(100);

		frame.getContentPane().add(progressBar, buildGBC(
				y++,
				HALF_PADDING,
				HALF_PADDING,
				GridBagConstraints.HORIZONTAL,
				null
		));

		JScrollPane resultPane = new JScrollPane();

		frame.getContentPane().add(buildLabelledComponent(
				"Result",
				resultPane,
				SwingConstants.CENTER,
				SwingConstants.LEFT,
				0
		), buildGBC(
				y++,
				HALF_PADDING,
				FULL_PADDING,
				GridBagConstraints.BOTH,
				1.0
		));

		resultPane.setViewportView(resultArea);

		frame.setVisible(true);
		frame.pack();
		frame.setLocationRelativeTo(null);
	}
}
