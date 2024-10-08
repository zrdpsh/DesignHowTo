0. Объявляем интерфейсы для двух главных задач - обмена данными с сервером и отрисовки UI
```java
public interface ServerConnection {
    void connect(String host, int port) throws IOException;
    int readInt() throws IOException;
    void writeInt(int value) throws IOException;
}

public interface GameUI {
    void updateTitle(String title);
    void updateStatus(String status);
    void setCellToken(int row, int col, char token);
}
```

1. Реализация ServerConnection
```java
public class SocketServerConnection implements ServerConnection {
    private DataInputStream fromServer;
    private DataOutputStream toServer;

    @Override
    public void connect(String host, int port) throws IOException {
        Socket socket = new Socket(host, port);
        fromServer = new DataInputStream(socket.getInputStream());
        toServer = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public int readInt() throws IOException {
        return fromServer.readInt();
    }

    @Override
    public void writeInt(int value) throws IOException {
        toServer.writeInt(value);
    }
}
```

1. Реализация GameUI
```java
public class JavaFXGameUI implements GameUI {
    private final Label lblTitle;
    private final Label lblStatus;
    private final Cell[][] cells;

    public JavaFXGameUI(Label lblTitle, Label lblStatus, Cell[][] cells) {
        this.lblTitle = lblTitle;
        this.lblStatus = lblStatus;
        this.cells = cells;
    }

    @Override
    public void updateTitle(String title) {
        Platform.runLater(() -> lblTitle.setText(title));
    }

    @Override
    public void updateStatus(String status) {
        Platform.runLater(() -> lblStatus.setText(status));
    }

    @Override
    public void setCellToken(int row, int col, char token) {
        Platform.runLater(() -> cells[row][col].setToken(token));
    }
}
```

2. Переписанный первоначальный класс TicTacToeClient. UI и коннект с сервером теперь можно менять независимо от клиента

```java
public class TicTacToeClient extends Application implements TicTacToeConstants {
    private boolean myTurn = false;
    private char myToken = ' ';
    private char otherToken = ' ';
    private final GameUI gameUI;
    private final ServerConnection serverConnection;
    private final Cell[][] cell = new Cell[3][3];
    private boolean continueToPlay = true;
    private boolean waiting = true;
    private int rowSelected;
    private int columnSelected;


    public TicTacToeClient(GameUI gameUI, ServerConnection serverConnection) {
        this.gameUI = gameUI;
        this.serverConnection = serverConnection;
    }

    @Override
    public void start(Stage primaryStage) {
        GridPane pane = new GridPane();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cell[i][j] = new Cell(i, j);
                pane.add(cell[i][j], j, i);
            }
        }

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(((JavaFXGameUI) gameUI).getLblTitle()); 
        borderPane.setCenter(pane);
        borderPane.setBottom(((JavaFXGameUI) gameUI).getLblStatus());

        Scene scene = new Scene(borderPane, 320, 350);
        primaryStage.setTitle("TicTacToeClient");
        primaryStage.setScene(scene);
        primaryStage.show();

        connectToServer();
    }

    private void connectToServer() {
        try {
            serverConnection.connect("localhost", 8000);
            new Thread(() -> {
                try {
                    int player = serverConnection.readInt();
                    handlePlayerConnection(player);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handlePlayerConnection(int player) throws IOException, InterruptedException {
        if (player == PLAYER1) {
            myToken = 'X';
            otherToken = 'O';
            gameUI.updateTitle("Player 1 with token 'X'");
            gameUI.updateStatus("Waiting for player 2 to join");
            serverConnection.readInt();
            gameUI.updateStatus("Player 2 has joined. I start first");
            myTurn = true;
        } else if (player == PLAYER2) {
            myToken = 'O';
            otherToken = 'X';
            gameUI.updateTitle("Player 2 with token 'O'");
            gameUI.updateStatus("Waiting for player 1 to move");
        }

        while (continueToPlay) {
            if (player == PLAYER1) {
                waitForPlayerAction();
                sendMove();
                receiveInfoFromServer();
            } else if (player == PLAYER2) {
                receiveInfoFromServer();
                waitForPlayerAction();
                sendMove();
            }
        }
    }

    private void waitForPlayerAction() throws InterruptedException {
        while (waiting) {
            Thread.sleep(100);
        }
        waiting = true;
    }

    private void sendMove() throws IOException {
        serverConnection.writeInt(rowSelected);
        serverConnection.writeInt(columnSelected);
    }

    private void receiveInfoFromServer() throws IOException {
        int status = serverConnection.readInt();
        if (status == PLAYER1_WON) {
            handlePlayerWon('X');
        } else if (status == PLAYER2_WON) {
            handlePlayerWon('O');
        } else if (status == DRAW) {
            continueToPlay = false;
            gameUI.updateStatus("Game is over, no winner!");
        } else {
            receiveMove();
            gameUI.updateStatus("My turn");
            myTurn = true;
        }
    }

    private void receiveMove() throws IOException {
        int row = serverConnection.readInt();
        int column = serverConnection.readInt();
        gameUI.setCellToken(row, column, otherToken);
    }

    private void handlePlayerWon(char winnerToken) {
        continueToPlay = false;
        if (myToken == winnerToken) {
            gameUI.updateStatus("I won! (" + winnerToken + ")");
        } else {
            gameUI.updateStatus("Player " + winnerToken + " has won!");
        }
    }
}

```

4. + Ещё один новый класс, чтобы собрать всё это вместе:
```java
public class TicTacToeClientLauncher {
    public static void main(String[] args) {
        Label lblTitle = new Label();
        Label lblStatus = new Label();
        Cell[][] cells = new Cell[3][3];


        GameUI gameUI = new JavaFXGameUI(lblTitle, lblStatus, cells);
        ServerConnection serverConnection = new SocketServerConnection();

        TicTacToeClient client = new TicTacToeClient(gameUI, serverConnection);
        Application.launch(client.getClass(), args);
    }
}

```


