package View;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.SashForm;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import AI.CONVOLUTION.BaseImage;
import AI.CONVOLUTION.Convolutional;
import AI.CONVOLUTION.TrainingItem;
import AI.NEURAL.ANN;
import FileIO.FileInput;
import View.Template.FormTemplate;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class TrainingTool extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text text;
	private Text text_message;
	private List list_dataset;
	private List list_TrainningData;
	protected int id = 0;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public TrainingTool(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(567, 374);
		shell.setText(getText());
		shell.setLayout(new GridLayout(2, false));
		new FormTemplate().setCenterScreen(shell);

		SashForm sashForm = new SashForm(shell, SWT.NONE);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		Composite composite = new Composite(sashForm, SWT.NONE);
		composite.setLayout(new GridLayout(3, false));

		Label label = new Label(composite, SWT.NONE);
		label.setText("Name: ");

		text = new Text(composite, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		Button btnAddTrainData = new Button(composite, SWT.NONE);
		btnAddTrainData.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String[] s = list_dataset.getItems();
				if (s.length < 0)
					return;
				ArrayList<BaseImage> bil = new ArrayList<>();
				for (String string : s) {
					Mat base = Imgcodecs.imread(((File) list_dataset.getData(string)).getPath());
					BaseImage bi = new BaseImage(base);
					bi.setId(id);
					bi.setName(text.getText());
					bil.add(bi);
				}
				for (BaseImage baseImage : bil) {
					list_TrainningData.add(baseImage.getId() + "");
					list_dataset.setData(baseImage.getName(), baseImage);
				}
				id++;
			}
		});
		btnAddTrainData.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnAddTrainData.setText("Add Train Data");

		Button button_1 = new Button(composite, SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				loadImage();
			}

			private void loadImage() {
				list_dataset.removeAll();
				ArrayList<File> imageFilelist = new FileInput().getImageList(shell);
				for (File file : imageFilelist) {
					list_dataset.add(file.getName());
					list_dataset.setData(file.getName(), file);
				}
			}
		});
		button_1.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		button_1.setText("Load data set");

		list_dataset = new List(composite, SWT.BORDER);
		list_dataset.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		list_TrainningData = new List(composite, SWT.BORDER);
		list_TrainningData.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		text_message = new Text(sashForm, SWT.BORDER | SWT.READ_ONLY | SWT.MULTI);
		sashForm.setWeights(new int[] { 364, 184 });

		Button button_3 = new Button(shell, SWT.NONE);
		button_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final ANN var = TrainNeural();
				result = var;
			}
		});
		GridData gd_button_3 = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1);
		gd_button_3.widthHint = 75;
		button_3.setLayoutData(gd_button_3);
		button_3.setText("CNN Train");

		Button button_2 = new Button(shell, SWT.NONE);
		GridData gd_button_2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_button_2.widthHint = 75;
		button_2.setLayoutData(gd_button_2);
		button_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		button_2.setText("Close");

	}

	protected ANN TrainNeural() {
		Random bigbang = new Random();
		ANN ann = new ANN(bigbang);
		ann.setTrainingData(getTrainingData());
		ann.ANN_RUN();
		return ann;
	}

	private ArrayList<TrainingItem> getTrainingData() {
		ArrayList<BaseImage> baseImageList = getBaseImage();
		Convolutional c = new Convolutional(baseImageList);
		return c.getTrainingData();
	}

	private ArrayList<BaseImage> getBaseImage() {
		String[] text = list_TrainningData.getItems();
		ArrayList<BaseImage> data = new ArrayList<>();
		for (String string : text) {
			if (list_TrainningData.getData(string) != null)
				data.add((BaseImage) list_TrainningData.getData(string));
		}
		return data;
	}

}
