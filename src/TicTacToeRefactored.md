Первоначальный вариант:
```java
public class TicTacToeClient extends Application
        implements TicTacToeConstants {
    // Indicate whether the player has the turn
    private boolean myTurn = false;

    // Indicate the token for the player
    private char myToken = ' ';

    // Indicate the token for the other player
    private char otherToken = ' ';

    // Create and initialize cells
    private Cell[][] cell =  new Cell[3][3];

    // Create and initialize a title label
    private Label lblTitle = new Label();

    // Create and initialize a status label
    private Label lblStatus = new Label();

    // Indicate selected row and column by the current move
    private int rowSelected;
    private int columnSelected;

    // Input and output streams from/to server
    private DataInputStream fromServer;
    private DataOutputStream toServer;

    // Continue to play?
    private boolean continueToPlay = true;

    // Wait for the player to mark a cell
    private boolean waiting = true;

    // Host name or ip
    private String host = "localhost";

    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) {
        // Pane to hold cell
        GridPane pane = new GridPane();
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                pane.add(cell[i][j] = new Cell(i, j), j, i);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(lblTitle);
        borderPane.setCenter(pane);
        borderPane.setBottom(lblStatus);

        // Create a scene and place it in the stage
        Scene scene = new Scene(borderPane, 320, 350);
        primaryStage.setTitle("TicTacToeClient"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage

        // Connect to the server
        connectToServer();
    }

    private void connectToServer() {
        try {
            // Create a socket to connect to the server
            Socket socket = new Socket(host, 8000);

            // Create an input stream to receive data from the server
            fromServer = new DataInputStream(socket.getInputStream());

            // Create an output stream to send data to the server
            toServer = new DataOutputStream(socket.getOutputStream());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        // Control the game on a separate thread
        new Thread(() -> {
            try {
                // Get notification from the server
                int player = fromServer.readInt();

                // Am I player 1 or 2?
                if (player == PLAYER1) {
                    myToken = 'X';
                    otherToken = 'O';
                    Platform.runLater(() -> {
                        lblTitle.setText("Player 1 with token 'X'");
                        lblStatus.setText("Waiting for player 2 to join");
                    });

                    // Receive startup notification from the server
                    fromServer.readInt(); // Whatever read is ignored

                    // The other player has joined
                    Platform.runLater(() ->
                            lblStatus.setText("Player 2 has joined. I start first"));

                    // It is my turn
                    myTurn = true;
                }
                else if (player == PLAYER2) {
                    myToken = 'O';
                    otherToken = 'X';
                    Platform.runLater(() -> {
                        lblTitle.setText("Player 2 with token 'O'");
                        lblStatus.setText("Waiting for player 1 to move");
                    });
                }

                // Continue to play
                while (continueToPlay) {
                    if (player == PLAYER1) {
                        waitForPlayerAction(); // Wait for player 1 to move
                        sendMove(); // Send the move to the server
                        receiveInfoFromServer(); // Receive info from the server
                    }
                    else if (player == PLAYER2) {
                        receiveInfoFromServer(); // Receive info from the server
                        waitForPlayerAction(); // Wait for player 2 to move
                        sendMove(); // Send player 2's move to the server
                    }
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    /** Wait for the player to mark a cell */
    private void waitForPlayerAction() throws InterruptedException {
        while (waiting) {
            Thread.sleep(100);
        }

        waiting = true;
    }

    /** Send this player's move to the server */
    private void sendMove() throws IOException {
        toServer.writeInt(rowSelected); // Send the selected row
        toServer.writeInt(columnSelected); // Send the selected column
    }

    /** Receive info from the server */
    private void receiveInfoFromServer() throws IOException {
        // Receive game status
        int status = fromServer.readInt();

        if (status == PLAYER1_WON) {
            // Player 1 won, stop playing
            continueToPlay = false;
            if (myToken == 'X') {
                Platform.runLater(() -> lblStatus.setText("I won! (X)"));
            }
            else if (myToken == 'O') {
                Platform.runLater(() ->
                        lblStatus.setText("Player 1 (X) has won!"));
                receiveMove();
            }
        }
        else if (status == PLAYER2_WON) {
            // Player 2 won, stop playing
            continueToPlay = false;
            if (myToken == 'O') {
                Platform.runLater(() -> lblStatus.setText("I won! (O)"));
            }
            else if (myToken == 'X') {
                Platform.runLater(() ->
                        lblStatus.setText("Player 2 (O) has won!"));
                receiveMove();
            }
        }
        else if (status == DRAW) {
            // No winner, game is over
            continueToPlay = false;
            Platform.runLater(() ->
                    lblStatus.setText("Game is over, no winner!"));

            if (myToken == 'O') {
                receiveMove();
            }
        }
        else {
            receiveMove();
            Platform.runLater(() -> lblStatus.setText("My turn"));
            myTurn = true; // It is my turn
        }
    }

    private void receiveMove() throws IOException {
        // Get the other player's move
        int row = fromServer.readInt();
        int column = fromServer.readInt();
        Platform.runLater(() -> cell[row][column].setToken(otherToken));
    }

    // An inner class for a cell
    public class Cell extends Pane {
        // Indicate the row and column of this cell in the board
        private int row;
        private int column;

        // Token used for this cell
        private char token = ' ';

        public Cell(int row, int column) {
            this.row = row;
            this.column = column;
            this.setPrefSize(2000, 2000); // What happens without this?
            setStyle("-fx-border-color: black"); // Set cell's border
            this.setOnMouseClicked(e -> handleMouseClick());
        }

        /** Return token */
        public char getToken() {
            return token;
        }

        /** Set a new token */
        public void setToken(char c) {
            token = c;
            repaint();
        }

        protected void repaint() {
            if (token == 'X') {
                Line line1 = new Line(10, 10,
                        this.getWidth() - 10, this.getHeight() - 10);
                line1.endXProperty().bind(this.widthProperty().subtract(10));
                line1.endYProperty().bind(this.heightProperty().subtract(10));
                Line line2 = new Line(10, this.getHeight() - 10,
                        this.getWidth() - 10, 10);
                line2.startYProperty().bind(
                        this.heightProperty().subtract(10));
                line2.endXProperty().bind(this.widthProperty().subtract(10));

                // Add the lines to the pane
                this.getChildren().addAll(line1, line2);
            }
            else if (token == 'O') {
                Ellipse ellipse = new Ellipse(this.getWidth() / 2,
                        this.getHeight() / 2, this.getWidth() / 2 - 10,
                        this.getHeight() / 2 - 10);
                ellipse.centerXProperty().bind(
                        this.widthProperty().divide(2));
                ellipse.centerYProperty().bind(
                        this.heightProperty().divide(2));
                ellipse.radiusXProperty().bind(
                        this.widthProperty().divide(2).subtract(10));
                ellipse.radiusYProperty().bind(
                        this.heightProperty().divide(2).subtract(10));
                ellipse.setStroke(Color.BLACK);
                ellipse.setFill(Color.WHITE);

                getChildren().add(ellipse); // Add the ellipse to the pane
            }
        }

        /* Handle a mouse click event */
        private void handleMouseClick() {
            // If cell is not occupied and the player has the turn
            if (token == ' ' && myTurn) {
                setToken(myToken);  // Set the player's token in the cell
                myTurn = false;
                rowSelected = row;
                columnSelected = column;
                lblStatus.setText("Waiting for the other player to move");
                waiting = false; // Just completed a successful move
            }
        }
    }

    /**
     * The main method is only needed for the IDE with limited
     * JavaFX support. Not needed for running from the command line.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
```
вариант в функциональном стиле:
```
public class TicTacToeClient extends Application implements TicTacToeConstants {

    // Immutable class to represent the state of the game
    public static final class GameState {
        private final boolean myTurn;
        private final char myToken;
        private final char otherToken;
        private final boolean continueToPlay;
        private final boolean waiting;
        private final int rowSelected;
        private final int columnSelected;
        private final Cell[][] cell;

        public GameState(boolean myTurn, char myToken, char otherToken,
                         boolean continueToPlay, boolean waiting,
                         int rowSelected, int columnSelected, Cell[][] cell) {
            this.myTurn = myTurn;
            this.myToken = myToken;
            this.otherToken = otherToken;
            this.continueToPlay = continueToPlay;
            this.waiting = waiting;
            this.rowSelected = rowSelected;
            this.columnSelected = columnSelected;
            this.cell = cell;
        }

        // Copy constructor to create new state
        public GameState withUpdatedTurn(boolean newTurn) {
            return new GameState(newTurn, myToken, otherToken, continueToPlay, waiting, rowSelected, columnSelected, cell);
        }

        public GameState withUpdatedCell(int row, int col, char token) {
            Cell[][] newCells = copyCells(cell);
            newCells[row][col].setToken(token);
            return new GameState(myTurn, myToken, otherToken, continueToPlay, waiting, row, col, newCells);
        }

        public GameState withWaiting(boolean waiting) {
            return new GameState(myTurn, myToken, otherToken, continueToPlay, waiting, rowSelected, columnSelected, cell);
        }

        private static Cell[][] copyCells(Cell[][] originalCells) {
            Cell[][] newCells = new Cell[originalCells.length][originalCells[0].length];
            for (int i = 0; i < originalCells.length; i++) {
                for (int j = 0; j < originalCells[i].length; j++) {
                    newCells[i][j] = new Cell(originalCells[i][j].getRow(), originalCells[i][j].getColumn(), originalCells[i][j].getToken());
                }
            }
            return newCells;
        }
    }

    // Immutable class to represent a move
    public static final class Move {
        private final int row;
        private final int column;

        public Move(int row, int column) {
            this.row = row;
            this.column = column;
        }

        public int getRow() {
            return row;
        }

        public int getColumn() {
            return column;
        }
    }

    // Data input and output are also immutable
    private final DataInputStream fromServer;
    private final DataOutputStream toServer;
    private final String host = "localhost";

    public TicTacToeClient() throws IOException {
        // Create a socket to connect to the server
        Socket socket = new Socket(host, 8000);
        this.fromServer = new DataInputStream(socket.getInputStream());
        this.toServer = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void start(Stage primaryStage) {
        // Set up the initial state
        GameState initialState = new GameState(false, ' ', ' ', true, true, -1, -1, new Cell[3][3]);
        GridPane pane = setupUI(initialState);

        BorderPane borderPane = new BorderPane();
        Label lblTitle = new Label();
        Label lblStatus = new Label();

        borderPane.setTop(lblTitle);
        borderPane.setCenter(pane);
        borderPane.setBottom(lblStatus);

        Scene scene = new Scene(borderPane, 320, 350);
        primaryStage.setTitle("TicTacToeClient");
        primaryStage.setScene(scene);
        primaryStage.show();

        connectToServer(initialState, lblTitle, lblStatus);
    }

    private GridPane setupUI(GameState initialState) {
        GridPane pane = new GridPane();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                pane.add(initialState.cell[i][j] = new Cell(i, j), j, i);
            }
        }
        return pane;
    }

    private void connectToServer(GameState state, Label lblTitle, Label lblStatus) {
        new Thread(() -> {
            try {
                int player = fromServer.readInt();
                GameState newState = initializePlayer(state, player, lblTitle, lblStatus);

                // Continue to play
                while (newState.continueToPlay) {
                    if (player == PLAYER1) {
                        newState = waitForPlayerAction(newState);
                        sendMove(newState);
                        newState = receiveInfoFromServer(newState, lblStatus);
                    } else if (player == PLAYER2) {
                        newState = receiveInfoFromServer(newState, lblStatus);
                        newState = waitForPlayerAction(newState);
                        sendMove(newState);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    private GameState initializePlayer(GameState state, int player, Label lblTitle, Label lblStatus) throws IOException {
        if (player == PLAYER1) {
            Platform.runLater(() -> {
                lblTitle.setText("Player 1 with token 'X'");
                lblStatus.setText("Waiting for player 2 to join");
            });
            fromServer.readInt(); // Ignore server startup info

            Platform.runLater(() -> lblStatus.setText("Player 2 has joined. I start first"));
            return state.withUpdatedTurn(true).withUpdatedCell(-1, -1, 'X');
        } else if (player == PLAYER2) {
            Platform.runLater(() -> {
                lblTitle.setText("Player 2 with token 'O'");
                lblStatus.setText("Waiting for player 1 to move");
            });
            return state.withUpdatedCell(-1, -1, 'O');
        }
        return state;
    }

    private GameState waitForPlayerAction(GameState state) throws InterruptedException {
        while (state.waiting) {
            Thread.sleep(100);
        }
        return state.withWaiting(true);
    }

    private void sendMove(GameState state) throws IOException {
        toServer.writeInt(state.rowSelected);
        toServer.writeInt(state.columnSelected);
    }

    private GameState receiveInfoFromServer(GameState state, Label lblStatus) throws IOException {
        int status = fromServer.readInt();
        return handleGameStatus(state, status, lblStatus);
    }

    private GameState handleGameStatus(GameState state, int status, Label lblStatus) throws IOException {
        if (status == PLAYER1_WON) {
            return handlePlayerWon(state, lblStatus, 'X', "Player 1 has won!");
        } else if (status == PLAYER2_WON) {
            return handlePlayerWon(state, lblStatus, 'O', "Player 2 has won!");
        } else if (status == DRAW) {
            Platform.runLater(() -> lblStatus.setText("Game is over, no winner!"));
            return state.withWaiting(false);
        } else {
            return state.withUpdatedTurn(true);
        }
    }

    private GameState handlePlayerWon(GameState state, Label lblStatus, char token, String message) throws IOException {
        Platform.runLater(() -> lblStatus.setText(state.myToken == token ? "I won!" : message));
        return state.withWaiting(false).withUpdatedTurn(false);
    }

    public static void main(String[] args) {
        launch(args);
    }

    // The inner Cell class remains largely the same but should also be immutable
    public static final class Cell extends Pane {
        private final int row;
        private final int column;
        private final char token;

        public Cell(int row, int column) {
            this(row, column, ' ');
        }

        public Cell(int row, int column, char token) {
            this.row = row;
            this.column = column;
            this.token = token;
            setPrefSize(2000, 2000);
            setStyle("-fx-border-color: black");
        }

        public int getRow() {
            return row;
        }

        public int getColumn() {
            return column;
        }

        public char getToken() {
            return token;
        }

        public void setToken(char token) {
            this.repaint(token);
        }

        protected void repaint(char token) {
            if (token == 'X') {
                Line line1 = new Line(10, 10, this.getWidth() - 10, this.getHeight() - 10);
                Line line2 = new Line(10, this.getHeight() - 10

```
