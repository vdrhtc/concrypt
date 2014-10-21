package view.chats;

import java.util.ArrayList;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.chats.Chats;
import view.SwitchableView;
import controller.ChatsController;
import controller.ViewSwitcher;
import data.DataOperator;

public class ChatsView implements SwitchableView {

	public static final int LOAD_NEW_COUNT = 2;
	public static final int CHATS_PER_PAGE = 10;
	public static final ViewName NAME = ViewName.CHATS_VIEW;

	public ChatsView() {
		this.controller = new ChatsController(this);
		
		this.root.getStyleClass().add("chats-root");
		this.header.getStyleClass().add("chats-header");
		this.statusMessage.setId("chats-status-message");
		this.statusBar.getStyleClass().add("chats-status-bar");
		this.chatsContainer.getStyleClass().add("chats-container");
		
	}
	
	public Chats getModel() {
		return model;
	}

	@Override
	public Pane buildRoot() {
		appendNewEntries(CHATS_PER_PAGE);
		
		accessExpirationInfo.setText("Доступ без пароля до "+DataOperator.getLastTokenExpirationDate());
		statusBar.getChildren().addAll(statusMessage, progressBar, accessExpirationInfo);
		
		root.setCenter(chatsContainer);
		root.setTop(header);
		root.setBottom(statusBar);
		return root;
	}

	public void appendNewEntries(int count) {
		ArrayList<Node> toAppend = new ArrayList<>();
		int oldChatEntriesCount = chatEntriesCount;
		for (int i = oldChatEntriesCount; i < oldChatEntriesCount+count; i++) {
			chatEntriesCount++;
			toAppend.add(buildHBorder());
			ChatEntryView newEntry = new ChatEntryView(model.getChats().get(i), chatsContainer.heightProperty());
			entries.add(newEntry);
			toAppend.add(newEntry.buildRoot());
			progressBar.setProgress(progressBar.getProgress() + 0.5*(oldChatEntriesCount+count-1));
		}
		chatsLayout.getChildren().addAll(toAppend);
	}

	public Integer getChatEntriesCount() {
		return chatEntriesCount;
	}

	private Separator buildHBorder() {
		Separator hBorder = new Separator(Orientation.HORIZONTAL);
		hBorder.getStyleClass().add("chats-border");
		return hBorder;
	}

	public void update() {
		for (int i = 0; i < entries.size(); i++) 
			entries.get(i).update(model.getChats().get(i));
	}
	
	private Chats model = new Chats();
	private ChatsController controller;
	private HBox statusBar = new HBox(); 
	private VBox chatsLayout = new VBox();
	private Integer chatEntriesCount = 0;
	private ArrayList<ChatEntryView> entries = new ArrayList<>();
	private ScrollPane chatsContainer = new ScrollPane(chatsLayout);

	private Label header = new Label("Чаты");
	private BorderPane root = new BorderPane();
	private Label statusMessage = new Label("Ready");
	private ProgressBar progressBar = new ProgressBar();
	private BooleanProperty updaterCanBeLaunched = new SimpleBooleanProperty(false);

	public BooleanProperty getReadyForUpdates() {
		return updaterCanBeLaunched;
	}

	public Label getStatusMessage() {
		return statusMessage;
	}

	private Label accessExpirationInfo = new Label();
	
	
	@Override
	public void setViewSwitcher(ViewSwitcher VS) {
		controller.setVS(VS);
	}

	@Override
	public Pane getRoot() {
		return root;
	}
	
	@Override
	public ViewName getName() {
		return ChatsView.NAME;
	}

	@Override
	public void prepareModel() {
		model.initializeEntries();
		updaterCanBeLaunched.setValue(true);
	}

	@Override
	public ViewName redirectTo() {
		return getName();
	}
	
	public ScrollPane getChatsContainer() {
		return chatsContainer;
	}
	
	public VBox getChatsLayout() {
		return chatsLayout;
	}
	
	public double getEntryHeight() {
		return this.root.getHeight()/CHATS_PER_PAGE;
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}

}
