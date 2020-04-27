import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class InstantMessenger {

    private final MainFrame frame;
    private final Chat chat;
    private String sender;
    private static final int SERVER_PORT = 4567;

    public InstantMessenger(MainFrame frame) {
        this.frame = frame;
        this.chat = new Chat();
        this.startServer();
    }

    private void startServer() {
        // Создание и запуск потока-обработчика запросов
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
                    while (!Thread.interrupted()) {
                        final Socket socket = serverSocket.accept();
                        final DataInputStream in = new DataInputStream(socket.getInputStream());
                        final String userName = in.readUTF();
                        final String message = in.readUTF();
                        socket.close();
                        final String address = chat.getAddress(sender);
                        // Выводим сообщение в текстовую область
                        frame.getTextAreaIncoming(chat.getUser(userName)).append(sender + " (" + address + "): " + message + "\n");  // для одного окошка
                       // frame.getTextAreaIncoming(chat.getUser(userName)).append(userName + ": " + message + "\n");       // для двух
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(frame,
                            "Ошибка в работе сервера", "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }).start();
    }

    public void sendMessage(User user, String recipientName, String message) throws IOException {
        if (sender == null) {
            JOptionPane.showMessageDialog(frame,
                    "Войдите в чат", "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (recipientName.isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                    "Введите имя получателя", "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (message.isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                    "Введите текст сообщения", "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!user.getName().equals(recipientName)) {
            JOptionPane.showMessageDialog(frame,
                    "Вы не можете отправить сообщение ползователю " + recipientName +
                            ". У вас открыт диалог с пользователем " + user.getName(), "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Создаем сокет для соединения
        final Socket socket = new Socket(user.getIp(), 4567);                             // для двух окошек 4566, для одного 4567
        // Открываем поток вывода данных
        final DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.writeUTF(user.getName());                                                                                    // для одного окошка
       // out.writeUTF(sender);                                                                                            // для двух
        out.writeUTF(message);
        socket.close();
        frame.getTextAreaIncoming(user).append("Я (" + sender + ")"  + " -> " + user.getName() + ": " + message + "\n");
        frame.getTextAreaOutgoing(user).setText("");
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }

    public User getUser(String name) {
        return chat.getUser(name);
    }

    public boolean checkName(String name) {
        return chat.search(name);
    }

    public void register(String name) {
        if (chat.getUsersBase().size() < 512) {
            chat.addUser(name);
        } else {
            JOptionPane.showMessageDialog(frame,
                    "В чате нет места", "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
