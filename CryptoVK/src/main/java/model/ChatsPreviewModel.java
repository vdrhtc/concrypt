package model;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;

import data.ReadStatesDatabase;
import http.ConnectionOperator;

public class ChatsPreviewModel implements Updated {

	public static final int PRE_LOADED_ENTRIES = 20;
	private Lock lock = new ReentrantLock();

	public ChatsPreviewModel() {
	}

	public void update(Object... params) {
		JSONArray chatsJSONs = CO.getDialogs(getChats().size(), 0);
		int i = 0;
		updateOrder(chatsJSONs);
		for (ChatPreviewModel e : chats) {
			e.update(chatsJSONs.getJSONObject(i).getJSONObject("message"));
			i++;
		}
	}

	private void updateOrder(JSONArray chatsJSONs) {
		for (int i = 0; i < chatsJSONs.length(); i++) {
			boolean matchFound = false;
			for (int j = 0; j < chats.size(); j++)
				if (chats.get(j).isContentCorresponding(chatsJSONs.getJSONObject(i).getJSONObject("message"))) {
					matchFound = true;
					if (j != i) {
						chats.add(i, chats.remove(j));
						break;
					}
				}
			if (!matchFound) {
				ChatPreviewModel newModel = new ChatPreviewModel();
				newModel.loadContent(chatsJSONs.getJSONObject(i).getJSONObject("message"));
				chats.add(0, newModel);
			}
		}
	}

	public ArrayList<ChatPreviewModel> getChats() {
		return chats;
	}

	@Override
	public void getLock(String takerName) {
		log.info("Getting lock: "+Thread.currentThread().getName()+" "+takerName);
		lock.lock();
		log.info("Got lock: "+Thread.currentThread().getName()+" "+takerName);
	}
	
	@Override
	public void releaseLock(String takerName) {
		lock.unlock();
		log.info("Releasing lock: "+Thread.currentThread().getName()+" "+takerName);

	}

	private ArrayList<ChatPreviewModel> chats = new ArrayList<>();
	private static ConnectionOperator CO = new ConnectionOperator(1000);

	public Integer getUnreadMessagesCount() {
		return ReadStatesDatabase.getUnreadCounter();
	}

	private static Logger log = Logger.getAnonymousLogger();

	static {
		log.setLevel(Level.ALL);
	}

	public static ConnectionOperator getConnectionOperator() {
		return CO;
	}
}
