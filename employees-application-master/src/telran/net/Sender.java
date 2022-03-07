package telran.net;

import java.io.Closeable;
import java.io.Serializable;

public interface Sender extends Closeable {
<T> T send(String requestType, Serializable requestData);
}
