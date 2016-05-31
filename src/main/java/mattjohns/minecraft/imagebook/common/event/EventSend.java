package mattjohns.minecraft.imagebook.common.event;

import java.util.ArrayList;
import java.util.List;

public class EventSend<T> {
	private List<EventReceive<T>> receiveList = new ArrayList<EventReceive<T>>();

	public void send(T item) {
		for (EventReceive<T> receive : receiveList) {
				receive.receive(item);
		}
	}

	public void add(EventReceive<T> method) {
		receiveList.add(method);
	}

	public void remove(EventReceive<T> method) {
		int index = receiveList.indexOf(method);
		
		if (index < 0 || index >= receiveList.size())
			return;
		
		receiveList.remove(index);
	}
	
	public void clear() {
		receiveList.clear();
	}
}
