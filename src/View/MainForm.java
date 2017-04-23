package View;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Table;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import FileIO.FileInput;
import View.Template.FormTemplate;

import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.custom.ScrolledComposite;

public class MainForm {

	protected Shell shell;
	private Table table_imagelist;
	private Text text;
	private Button btnGrayImage;
	private ArrayList<Rect> face = new ArrayList<>();
	private List listFace;
	private Label label_Face;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainForm window = new MainForm();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setImage(SWTResourceManager.getImage(MainForm.class, "/javax/swing/plaf/basic/icons/JavaCup16.png"));
		shell.setSize(728, 426);
		shell.setText("SWT Application");
		shell.setLayout(new GridLayout(1, false));
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		new FormTemplate().setCenterScreen(shell);

		SashForm sashForm = new SashForm(shell, SWT.NONE);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));

		Composite composite = new Composite(sashForm, SWT.NONE);
		composite.setLayout(new GridLayout(3, false));

		Button btnImportImage = new Button(composite, SWT.NONE);
		btnImportImage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				loadImage();
			}

			private void loadImage() {
				ArrayList<File> imageFilelist = new FileInput().getImageList(shell);
				for (File file : imageFilelist) {
					TableItem ti = new TableItem(table_imagelist, SWT.NONE);
					ti.setText(file.getName());
					ti.setData(file);
				}
			}
		});
		btnImportImage.setText("Import Image");

		Button btnGetListFace = new Button(composite, SWT.NONE);
		btnGetListFace.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnGetListFace.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem til[] = table_imagelist.getSelection();
				if (til.length <= 0)
					return;
				File file = (File) til[0].getData();
				CascadeClassifier classifier = loadCascadeClassifier();
				Mat SRC = Imgcodecs.imread(file.getPath());
				MatOfRect detections = new MatOfRect();
				Mat DST = new Mat();
				if (btnGrayImage.getSelection()) {
					Imgproc.cvtColor(SRC, DST, Imgproc.COLOR_RGB2GRAY);
				} else
					DST = SRC;
				classifier.detectMultiScale(DST, detections);
				ArrayList<MatImage> model = new ArrayList<>();
				// Draw a bounding box around each face.
				for (Rect rect : detections.toArray()) {
					double anpha = 0.2;
					double beta = anpha;
					int x = (int) (rect.x - rect.width * anpha);
					if (x < 0)
						x = rect.x;
					int y = (int) (rect.y - rect.height * beta);
					if (y < 0)
						y = rect.y;
					int w = (int) (rect.width * (1 + 2 * anpha));
					int h = (int) (rect.height * (1 + 2 * beta));
					Rect newrect = new Rect(x, y, (w + x) < SRC.width() ? w : rect.width,
							(h + y) < SRC.height() ? h : rect.height);
					face.add(newrect);
					model.add(new MatImage("Face x: " + rect.x + ", y:" + rect.y, SRC.submat(newrect)));
				}
				for (MatImage m : model) {
					listFace.add(m.toString());
					listFace.setData(m.toString(), m);
				}
			}
		});
		btnGetListFace.setText("Add list face");

		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				listFace.removeAll();
			}
		});
		btnNewButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnNewButton.setText("Clear");

		table_imagelist = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table_imagelist.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 2));
		table_imagelist.setHeaderVisible(true);
		table_imagelist.setLinesVisible(true);

		TableColumn tblclmnSource = new TableColumn(table_imagelist, SWT.NONE);
		tblclmnSource.setWidth(150);
		tblclmnSource.setText("Source");

		btnGrayImage = new Button(composite, SWT.CHECK);
		btnGrayImage.setText("grayimage");
		new Label(composite, SWT.NONE);

		listFace = new List(composite, SWT.BORDER | SWT.V_SCROLL);
		listFace.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String[] select = listFace.getSelection();
				if (select.length <= 0)
					return;
				MatImage mi = (MatImage) listFace.getData(select[0]);
				viewImage(mi);
			}
		});
		GridData gd_listFace = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		gd_listFace.widthHint = 145;
		listFace.setLayoutData(gd_listFace);

		Composite composite_1 = new Composite(sashForm, SWT.NONE);
		composite_1.setLayout(new GridLayout(3, false));

		Button btnTraining = new Button(composite_1, SWT.NONE);
		btnTraining.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TrainingTool t = new TrainingTool(shell, SWT.DIALOG_TRIM);
				t.open();
			}
		});
		btnTraining.setText("Training tool");
		new Label(composite_1, SWT.NONE);
		new Label(composite_1, SWT.NONE);

		Button btnGetDestFace = new Button(composite_1, SWT.NONE);
		btnGetDestFace.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnGetDestFace.setText("detectFace");

		Label lblName = new Label(composite_1, SWT.NONE);
		lblName.setText("result: ");

		text = new Text(composite_1, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		ScrolledComposite scrolledComposite = new ScrolledComposite(composite_1,
				SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		label_Face = new Label(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(label_Face);
		scrolledComposite.setMinSize(label_Face.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		sashForm.setWeights(new int[] { 324, 297 });

	}

	public BufferedImage Mat2BufferedImage(Mat m) {
		// Fastest code
		// output can be assigned either to a BufferedImage or to an Image

		int type = BufferedImage.TYPE_BYTE_GRAY;
		if (m.channels() > 1) {
			type = BufferedImage.TYPE_3BYTE_BGR;
		}
		int bufferSize = m.channels() * m.cols() * m.rows();
		byte[] b = new byte[bufferSize];
		m.get(0, 0, b); // get all the pixels
		BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
		final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(b, 0, targetPixels, 0, b.length);
		return image;
	}

	protected void viewImage(MatImage mi) {

		BufferedImage ii = Mat2BufferedImage(mi.getMat());
		if (ii != null) {
			Rectangle rect = label_Face.getBounds();
			int width = rect.width;
			int height = rect.height;
			float imageWidth = ii.getWidth();
			float imageHeight = ii.getHeight();
			float tiso = imageWidth / imageHeight;
			try {
				if (!label_Face.isDisposed() && width > 0 && height > 0) {
					if (!label_Face.isDisposed()) {
						label_Face.setSize(rect.width + 1, (int) (rect.width / tiso) + 1);
						new FileInput();
						label_Face.setImage(new Image(shell.getDisplay(), FileInput.convertToSWT(ii)));
					}
				}
			} catch (Exception e) {
				System.err.println(e);
			}
		}
	}

	protected CascadeClassifier loadCascadeClassifier() {
		CascadeClassifier classifier = new CascadeClassifier("./data/haarcascade_frontalface_alt.xml");
		return classifier;
	}

}
