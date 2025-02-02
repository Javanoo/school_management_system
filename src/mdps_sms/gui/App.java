package mdps_sms.gui;

import java.util.HashMap;
import java.util.TreeSet;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import mdps_sms.Main;
import mdps_sms.util.Administrator;
import mdps_sms.util.Fleet;
import mdps_sms.util.Person;
import mdps_sms.util.SchoolCalendar;
import mdps_sms.util.SchoolClass;
import mdps_sms.util.Staff;
import mdps_sms.util.Student;
import mdps_sms.util.Teacher;

public class App extends BorderPane {

	private TreeSet<Student> studentData = new TreeSet<>();
	private TreeSet<Teacher> teacherData = new TreeSet<>();
	private TreeSet<Staff> staffData = new TreeSet<>();
	private TreeSet<Fleet> fleetData = new TreeSet<>();
	private SchoolCalendar calendarData = new SchoolCalendar(3);
	private TreeSet<SchoolClass> classroomsData = new TreeSet<>();

	//private Settings settingsForm = new Settings();
	//private ItemList<Student> studentItemList;
	//private ItemList<Teacher> teacherItemList;
	private ItemList<Staff> staffItemList;
	private ItemList<Student> studentItemList;
	private ItemList<Teacher> teacherItemList;
	private ItemList<Fleet> fleetItemList;
	private CalendarTable calendar = new CalendarTable(calendarData.getDates());
	private FeesList feesList = new FeesList(studentData);

	private Dialog logoutDialog = new Dialog();

	Administrator admin;

	private static BorderPane centralPanel = new BorderPane();

	private HBox profileBox = new HBox();
	private Label loggedUser = new Label("Admin");

	private Label forDashboard = new Label("DashBoard");
	private Label forStudents = new Label("Students");
	private Label forTeachers = new Label("Teachers");
	private Label forStaff = new Label("Staff");
	private Label forClasses = new Label("Classes");
	private Label forCalendar = new Label("Calendar");
	private Label forExams = new Label("Exams");
	private Label forFees = new Label("Fees");
	private Label forFleet = new Label("Fleet");
	private ListView<Labeled> navigation = new ListView<>();

	private Button settings = new Button("Settings");
	private Button logout = new Button("Logout");

	private ContextMenu conMenu = new ContextMenu();

	private VBox utilButtons = new VBox();
	public static BorderPane leftPanelContents = new BorderPane();

	HashMap<String, Object> navTable = new HashMap<>();

	//testing
	TreeSet<Person> list = new TreeSet<>();

	public App() {}

	public App(Administrator admin, TreeSet<Student> studentData, TreeSet<Teacher> teacherData, TreeSet<Staff> staffData, TreeSet<Fleet> fleetData,
			TreeSet<SchoolClass> classrooms) {
		this.admin = admin;
		this.studentData = studentData;
		this.teacherData = teacherData;
		this.staffData = staffData;
		this.fleetData = fleetData;
		this.classroomsData = classrooms;

		staffItemList = new ItemList<>(new Staff(),this.staffData);
		studentItemList = new ItemList<>(new Student(), this.studentData);
		teacherItemList = new ItemList<>(new Teacher(), this.teacherData);
		fleetItemList = new ItemList<>(new Fleet(), this.fleetData);
		
		feesList = new FeesList(studentData);

		navTable.put("DashBoard", null);
		navTable.put("Students", studentItemList);
		navTable.put("Teachers", teacherItemList);
		navTable.put("Staff", staffItemList);
		navTable.put("Calendar", calendar);
		navTable.put("Exams", null);
		navTable.put("Fees", feesList);
		navTable.put("Fleet", fleetItemList);

		//navigation tabs
		forDashboard.setGraphic(UiComponents.createIcon("comboChartBlack.png", 24));
		styleNavElem(forDashboard);

		forStudents.setGraphic(UiComponents.createIcon("studentBlack.png", 24));
		styleNavElem(forStudents);

		forTeachers.setGraphic(UiComponents.createIcon("teacherBlack.png", 24));
		styleNavElem(forTeachers);

		forStaff.setGraphic(UiComponents.createIcon("staffBlack.png", 24));
		styleNavElem(forStaff);
		forStaff.setOnKeyPressed(e -> {if(e.getCode().equals(KeyCode.TAB)) centralPanel.requestFocus();});

		forClasses.setGraphic(UiComponents.createIcon("userBlack.png", 24));
		styleNavElem(forClasses);

		forCalendar.setGraphic(UiComponents.createIcon("calendarBlack.png", 24));
		styleNavElem(forCalendar);

		forExams.setGraphic(UiComponents.createIcon("courseAssignBlack.png", 24));
		styleNavElem(forExams);

		forFees.setGraphic(UiComponents.createIcon("cashBlack.png", 24));
		styleNavElem(forFees);

		forFleet.setGraphic(UiComponents.createIcon("shuttleBusBlack.png", 24));
		styleNavElem(forFleet);

		navigation.setItems(FXCollections.observableArrayList(forDashboard, forStudents,
				forTeachers, forCalendar, forExams, forStaff, forClasses, forFees, forFleet));
		navigation.setMaxHeight(400);
		navigation.setFixedCellSize(44);
		navigation.setMaxWidth(200);
		navigation.setId("mainNav");
		navigation.getSelectionModel().selectFirst();
		navigation.getSelectionModel().selectedItemProperty().addListener(e ->
			{
				switchView((Node)navTable.get(navigation.getSelectionModel().getSelectedItem().getText()));
			}

		);
		Rectangle navRec = new Rectangle(200, 400);
		navRec.setArcHeight(15);
		navRec.setArcWidth(15);
		navigation.setClip(navRec);


		//bottom action buttons
		settings.setGraphic(UiComponents.createIcon("settingsBlack.png", 22));
		styleNavElem(settings);
		settings.setMinWidth(120);
		settings.setAlignment(Pos.CENTER_LEFT);
		settings.setPadding(new Insets(5, 0, 5, 10));
		settings.setStyle("-fx-background-color: white");
		settings.setOnAction(e -> {
			switchView(new Settings(list));
			leftPanelContents.setDisable(true);
		});
		Rectangle setRec = new Rectangle(120, 33);
		setRec.setArcHeight(30);
		setRec.setArcWidth(30);
		settings.setClip(setRec);

		logout.setGraphic(UiComponents.createIcon("logOutBlack.png", 22));
		styleNavElem(logout);
		logout.setMinWidth(200);
		logout.setAlignment(Pos.CENTER_LEFT);
		logout.setPadding(new Insets(5, 0, 5, 10));
		logout.setStyle("-fx-background-color: white");
		logout.setOnAction(e -> logout());
		logout.setTooltip(new Tooltip("end session"));
		Rectangle logRec = new Rectangle(200, 33);
		logRec.setArcHeight(20);
		logRec.setArcWidth(20);
		logout.setClip(logRec);

		utilButtons.getChildren().addAll(settings, logout);
		utilButtons.setSpacing(10);

		leftPanelContents.setTop(navigation);
		leftPanelContents.setBottom(utilButtons);
		BorderPane.setAlignment(navigation, Pos.CENTER);
		leftPanelContents.setPadding(new Insets(5,10,7,10));
		leftPanelContents.setStyle("-fx-background-color: #232323");

		centralPanel.setPadding(new Insets(5, 5, 0, 5));
		centralPanel.setCenter(null);
		Rectangle centRec = new Rectangle();
		centRec.setArcHeight(30);
		centRec.setArcWidth(30);
		//centRec.widthProperty().bind(centralPanel.widthProperty());
		//centRec.heightProperty().bind(centralPanel.heightProperty().add(100));
		//centralPanel.setClip(centRec);


		setLeft(leftPanelContents);
		setCenter(centralPanel);
		//setStyle("-fx-background-color: #454545");
	}

	public void logout() {
		admin.setSession(0);
		Main.saveData(admin, Main.STORAGEFILE1);
		Main.primaryStage.close();
	}

	public static void switchView(Node s) {
		centralPanel.getChildren().clear();
		centralPanel.setCenter(s);
		fadeIn(centralPanel);
	}

	public static void fadeIn(Node E) {
		FadeTransition fade = new FadeTransition(new Duration(300), E);
		fade.setInterpolator(Interpolator.EASE_BOTH);
		fade.setAutoReverse(true);
		fade.setCycleCount(1);
		fade.setFromValue(0.2);
		fade.setToValue(1);
		fade.play();
	}
	protected void styleNavElem(Labeled label) {
		label.setTextFill(Color.BLACK);
		label.setFont(Font.font("Inter SemiBold", 15));
		label.setPadding(new Insets(5, 0, 5, 0));
		label.setGraphicTextGap(10);
	}
}

