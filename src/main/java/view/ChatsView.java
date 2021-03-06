package view;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import model.ChatsModel;
import model.Updated;

public class ChatsView implements View, Updated {

	public ChatsView() {
		initRoot();
	}

	private void initRoot() {
		
		root.getStyleClass().add("chats-root");
		chatNamesContainer.getStyleClass().add("chats-names-container");
		back.getStyleClass().add("chats-back-button");
		header.getStyleClass().add("chats-header");
		HBox.setHgrow(chatNamesContainer, Priority.ALWAYS);
		
		back.setCancelButton(true);
		header.getChildren().addAll(back, chatNamesContainer);
		root.setTop(header);
	}

	public void setCurrentViewedChat(ChatView CV) {
		this.root.setCenter(CV.getRoot());
	}
	

	public HashMap<Long, ChatView> getViewedChats() {
		return viewedChats;
	}

	@Override
	public void update(Object... params) {
		for(ChatView CV : viewedChats.values()) {
			CV.update();
		}
	}

	private HBox header = new HBox();
	private Button back = new Button("");
	private BorderPane root = new BorderPane();
	private ChatsModel model = new ChatsModel();
	private FlowPane chatNamesContainer = new FlowPane();
	private HashMap<Long, ChatView> viewedChats = new HashMap<>();
	private BooleanProperty canBeUpdated = new SimpleBooleanProperty();

	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(ChatsView.class);
	
	public Button getBackButton() {
		return back;
	}

	@Override
	public Pane getRoot() {
		return root;
	}

	@Override
	public ViewName getName() {
		return ViewName.CHATS_VIEW;
	}

	public ChatsModel getModel() {
		return model;
	}

	@Override
	public void getLock(String takerName) {
		model.getLock(takerName);
	}

	@Override
	public void releaseLock(String takerName) {
		model.releaseLock(takerName);
	}

	public BooleanProperty canBeUpdated() {
		return canBeUpdated;
	}

	public FlowPane getChatNamesContainer() {
		return chatNamesContainer;
	}

}
