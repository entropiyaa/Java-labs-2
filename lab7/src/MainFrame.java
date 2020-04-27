import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

    private final InstantMessenger messenger;
    private Map<User, Panel> panelMap = new HashMap<>();
    private static final String FRAME_TITLE = "Клиент мгновенных сообщений";
    private static final int FRAME_MINIMUM_WIDTH = 500;
    private static final int FRAME_MINIMUM_HEIGHT = 500;

    public InstantMessenger getMessenger() {
        return messenger;
    }

    public MainFrame() {
        super(FRAME_TITLE);
        messenger = new InstantMessenger(MainFrame.this);
        setMinimumSize(new Dimension(FRAME_MINIMUM_WIDTH, FRAME_MINIMUM_HEIGHT));
        final Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - getWidth()) / 2,
                (kit.getScreenSize().height - getHeight()) / 2);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        getContentPane().add(tabbedPane);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu menu = new JMenu("Функции");
        menuBar.add(menu);

        Action login = new AbstractAction("Вход") {
            public void actionPerformed(ActionEvent event) {
                String sender = JOptionPane.showInputDialog(MainFrame.this, "Введите имя",
                        "Вход", JOptionPane.QUESTION_MESSAGE);
                if (!messenger.checkName(sender)) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Пользователя нет в базе", "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    messenger.setSender(sender);
                }
            }
        };
        menu.add(login);

        Action registration = new AbstractAction("Регистрация") {
            public void actionPerformed(ActionEvent event) {
                String name = JOptionPane.showInputDialog(MainFrame.this, "Введите имя",
                        "Регистрация", JOptionPane.QUESTION_MESSAGE);
                if (messenger.checkName(name)) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Пользователь с таким именем уже существует", "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    messenger.register(name);
                }
            }
        };
        menu.add(registration);

        Action searchUser = new AbstractAction("Найти пользователя") {
            public void actionPerformed(ActionEvent event) {
                String name = JOptionPane.showInputDialog(MainFrame.this, "Введите имя пользователя",
                        "Поиск", JOptionPane.QUESTION_MESSAGE);
                if (!messenger.checkName(name)) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Пользователя не существует", "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Есть такой пользователь", "Результат",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        };
        menu.add(searchUser);

        Action createTab = new AbstractAction("Написать..") {
            public void actionPerformed(ActionEvent event) {
                String name = JOptionPane.showInputDialog(MainFrame.this, "Введите имя пользователя",
                        "Поиск", JOptionPane.QUESTION_MESSAGE);
                if (!messenger.checkName(name)) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Пользователя не существует", "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    User user = messenger.getUser(name);
                    panelMap.put(user, new Panel(MainFrame.this, user));
                    tabbedPane.addTab(name, panelMap.get(user).makePanel());
                }
            }
        };
        menu.add(createTab);
    }

    public JTextArea getTextAreaIncoming(User user) {
        return panelMap.get(user).getTextAreaIncoming();
    }

    public JTextArea getTextAreaOutgoing(User user) {
        return panelMap.get(user).getTextAreaOutgoing();
    }

    public static void main (String[]args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final MainFrame frame = new MainFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}