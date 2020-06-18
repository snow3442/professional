package server;

import common.*;

import java.io.IOException;
import java.util.*;

/**
 * An implementation of the Hub class tailored towards the game needs
 */
public class GardenWarHub extends Hub {

    private static int num_connected_players = 0;
    private final int ROWS = 8;
    private final int COLS = 8;
    private static GameState gameState = GameState.NORMAL;
    private ArrayList<Player> playerList = new ArrayList<>();
    private Player currentPlayer = new Player(0, "");
    private static final Map<String, Integer> itemCostMap;
    private TurnManager turnManager = new TurnManager();
    private ArrayList<Square> board = new ArrayList<>();
    private static int temperature = 15;
    private int HURRICANE_RATE = 0;
    private int PEST_RATE = 0;
    private int WILDFIRE_RATE = 0;
    private int FLOOD_RATE = 0;
    private int DROUGHT_RATE = 0;
    private boolean greenPolicyApplied = false;

    static {
        itemCostMap = new HashMap<>();
        itemCostMap.put("PATATO SEED", 1);
        itemCostMap.put("PUMPKIN SEED", 1);
        itemCostMap.put("BEANS SEED", 1);
        itemCostMap.put("GOURD SEED", 1);
        itemCostMap.put("RADISH SEED", 1);
        itemCostMap.put("SPINACH SEED", 1);
        itemCostMap.put("FERTILIZER", 1);
        itemCostMap.put("PESTICIDE", 1);
        itemCostMap.put("PLOWER", 2);
        itemCostMap.put("COLLECT", 1);
        itemCostMap.put("STARVATION", 1);
        itemCostMap.put("PLANT TREE", 1);
        itemCostMap.put("GREEN ENERGY", 2);
        itemCostMap.put("ACCEPT REFUGEES", 2);
        itemCostMap.put("WAR", 3);
    }
    //stores all climate information
    private static HashMap<String, Integer> climateMap;

    static {
        climateMap = new HashMap<>();
        climateMap.put("HURRICANE", 0);
        climateMap.put("WILDFIRE", 0);
        climateMap.put("FLOOD", 0);
        climateMap.put("PEST", 0);
        climateMap.put("DROUGHT", 0);
    }

    public GardenWarHub(int port) throws IOException {
        super(port);
        initBoard();
    }

    /**
     * initialized board model
     */
    private void initBoard() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                Square square = new Square();
                square.setxCoor(i);
                square.setyCoor(j);
                board.add(square);
            }
        }
    }

    /**
     * get the square object from (x,y) coordinates
     * @param x {int} x coordinate of the square to find
     * @param y {int} y coordinate of the square to find
     * @return {Square} the Square that has (x,y) as coordinate
     */
    private Square getSquare(int x, int y) {
        for (Square s : board) {
            if (x == s.getxCoor() && y == s.getyCoor()) {
                return s;
            }
        }
        return null;
    }

    /**
     * processes that needs to be done when players connect
     * @param playerID
     */
    protected void playerConnected(int playerID) {

    }

    /**
     * this method handles the Message sent by the Player with ID = playerID
     * according to the type of message received update the player's UI as
     * well as other players UI accordingly.
     *
     * @param playerID The ID number of the player who sent the message.
     * @param message  The message that was received from the player.
     */
    protected void messageReceived(int playerID, Object message) {
        //deal with log ins
        if (message instanceof LogInMessage) {
            if (num_connected_players < 2) {
                num_connected_players++;
                //init resources for the new player
                Player newPlayer = new Player(playerID, ((LogInMessage) message).getUsername());
                playerList.add(newPlayer);
                ArrayList<String> namesToSend = new ArrayList<>();
                for (Player p : playerList) {
                    namesToSend.add(p.getUsername());
                }
                sendToAll(new LogInUpdate(playerID, namesToSend));
            }
            //if there are 2 players, start game
            if (num_connected_players == 2) {
                startGame();
            }

        } //when user clicks on end turn button
        else if (message instanceof EndTurnRequest) {
            if (matchCurrentId(playerID)) {
                endTurn();
            }

        } //when user clicks a square
        else if (message instanceof SquareClickRequest) {
            SquareClickRequest request = (SquareClickRequest) message;
            int x = request.getX();
            int y = request.getY();
            if (matchCurrentId(playerID)) {
                //process current player requests
                if (!currentPlayer.getUsedItem().isEmpty()) {
                    apply(playerID, currentPlayer.getUsedItem().pop(), getSquare(x, y));
                }
            }
        }

        //functionalities limited to normal gamestate and current Turn
        if (matchCurrentId(playerID) && gameState.equals(GameState.NORMAL)) {
            if (message instanceof UseRequest) {
                //handles the use
                UseRequest request = (UseRequest) message;
                String itemName = request.getItemName();
                getPlayerByID(playerID).getUsedItem().push(itemName);
                processUseRequest(playerID, itemName);
                //if non clickable, do nothing
            } else if (message instanceof PurchaseRequest) {
                String itemName = ((PurchaseRequest) message).getItemName();
                int cost = itemCostMap.get(itemName);
                int remainingBalance = getPlayerByID(playerID).getActionPoints();
                if (remainingBalance < cost) {
                    sendToOne(playerID, "INVALID PURCHASE");
                } else {
                    getPlayerByID(playerID).setActionPoints(remainingBalance - cost);
                    getPlayerByID(playerID).addItemToPlayer(itemName);
                    sendToAll(new StatsUpdate(playerID, getPlayerByID(playerID).getActionPoints(),
                            "actionpoints"));
                    sendToOne(playerID, new PurchaseUpdate(
                            itemName, getPlayerByID(playerID).getItemsMap().get(itemName)));
                }
            } else if (message instanceof SellRequest) {
                SellRequest request = (SellRequest) message;
                String itemName = request.getItemName();
                int cost = itemCostMap.get(itemName);
                int remainingBalance = getPlayerByID(playerID).getActionPoints();
                getPlayerByID(playerID).setActionPoints(remainingBalance + cost);
                getPlayerByID(playerID).removeItemFromPlayer(itemName);
                sendToAll(new StatsUpdate(playerID, getPlayerByID(playerID).getActionPoints(),
                        "actionpoints"));
                sendToOne(playerID, new SellUpdate(itemName, getPlayerByID(playerID).getItemsMap().get(itemName)));
            }

        } else if (matchCurrentId(playerID) && gameState.equals(GameState.SELECTION)) {
            if (message instanceof String) {
                String s = (String) message;
                if (s.equals("UNSELECTALL")) {
                    sendToOne(currentPlayer.getPlayerID(), "UNSELECTALL");
                    unselectAllSquares();
                }
            }
        }


    }

    /**
     * start the game with player1 as current player
     */
    private void startGame() {
        currentPlayer = playerList.get(0);
        currentPlayer.setConsumption(2);
        currentPlayer.setActionPoints(5);
        currentPlayer.setFood(10);
        sendToAll(new StatsUpdate(currentPlayer.getPlayerID(), 10, "food"));
        sendToAll(new StatsUpdate(currentPlayer.getPlayerID(), 5, "actionpoints"));
        sendToAll(new StatsUpdate(currentPlayer.getPlayerID(), 2, "consumption"));
        sendToAll(new StatsUpdate(currentPlayer.getPlayerID(), currentPlayer.getActionIncremenent(), "earning"));
        turnManager.setSmallTurn(0);
        turnManager.setTurn(0);
        sendToAll(new TurnChangeUpdate(currentPlayer.getPlayerID()));
    }

    /**
     * end turn and update resources, check whether each plant needs to grow
     * and update climate probabilities and values
     */

    private void endTurn() {

        /**
         * The big turn increments by 1 when both players has clicked on end
         * the small turn increments by when any player has clicked on end
         */
        switchPlayer();
        sendToAll(new TurnChangeUpdate(currentPlayer.getPlayerID()));
        //if the small turn is 0, don't update big turn
        //because one complete round is not finished
        if (turnManager.getSmallTurn() == 0) {
            turnManager.setSmallTurn(1);

        } //if small turn is 1, meaning we finish a complete round
        else {
            turnManager.setSmallTurn(0);
            turnManager.setTurn(turnManager.getTurn() + 1);
            climateChange();
            //remove green energy policy
            greenPolicyApplied = false;
            for (Player player : playerList) {
                for (Plant plant : player.getPlants()) {
                    plant.grow();
                    processPlayerPlants(player.getPlayerID(), plant.getMessageObjects());
                }
            }
        }

        //for the first turn, load resources for the second player
        if (turnManager.getSmallTurn() == 1 && turnManager.getTurn() == 0) {
            currentPlayer.setFood(10);
            currentPlayer.setConsumption(2);
            currentPlayer.setActionPoints(5);
            currentPlayer.setActionIncremenent(5);
            sendToAll(new StatsUpdate(currentPlayer.getPlayerID(), 10, "food"));
            sendToAll(new StatsUpdate(currentPlayer.getPlayerID(), 5, "actionpoints"));
            sendToAll(new StatsUpdate(currentPlayer.getPlayerID(), 2, "consumption"));
            sendToAll(new StatsUpdate(currentPlayer.getPlayerID(), 5, "earning"));
        } else {
            //start turn initialization
            //increment action points
            currentPlayer.setActionPoints(currentPlayer.getActionPoints() + currentPlayer.getActionIncremenent());
            //consume food
            currentPlayer.setFood(currentPlayer.getFood() - currentPlayer.getConsumption());
            //if the current player runs out of good, game ends
            if (currentPlayer.getFood() < 0) {
                Player winner = getPlayerByID(3 - currentPlayer.getPlayerID());
                String message = currentPlayer.getUsername() + " starved to death! " +
                        winner.getUsername() + " has won the game! ";
                sendToAll(new EndGameSignal(message));
                System.out.println("cleaning resources...");
                //clean up resources
                cleanUpResources();

            }
            //update UI
            sendToAll(new StatsUpdate(currentPlayer.getPlayerID(), currentPlayer.getFood(), "food"));
            sendToAll(new StatsUpdate(currentPlayer.getPlayerID(), currentPlayer.getActionPoints(), "actionpoints"));
        }

        //update triggerables
    }

    /**
     * Broadcast weather events occurrence according to probabilities
     * and update the probabilities for the next turn
     */
    private void climateChange() {
        for (Map.Entry me : climateMap.entrySet()) {
            disaster(me.getKey().toString(), Integer.parseInt(me.getValue().toString()));
        }

        /**
         * update climate acceleration rate
         * if temperature has risen for
         * 1~3 degree: acceleration rate randomly increase by 1~3, 2~6, 3~9
         * degrees respectively
         * 4~6 degree: acceleration rate randomly increase by 10~15
         * 7 degree: rates go to 100%, irreversible
         */
        int temperatureRise = temperature - 15;
        System.out.println("current temperature: " + temperature);
        //stop climate change if green energy applied
        if (!greenPolicyApplied) {
            if (temperatureRise == 0) {
                HURRICANE_RATE += 1;
                WILDFIRE_RATE += 1;
                DROUGHT_RATE += 1;
                FLOOD_RATE += 1;
                PEST_RATE += 1;
            } else if (temperatureRise <= 3 && temperatureRise >= 1) {
                HURRICANE_RATE += new Random().nextInt(temperatureRise * 2 + 1) + temperatureRise;
                WILDFIRE_RATE += new Random().nextInt(temperatureRise * 2 + 1) + temperatureRise;
                DROUGHT_RATE += new Random().nextInt(temperatureRise * 2 + 1) + temperatureRise;
                FLOOD_RATE += new Random().nextInt(temperatureRise * 2 + 1) + temperatureRise;
                PEST_RATE += new Random().nextInt(temperatureRise * 2 + 1) + temperatureRise;
            } else if (temperatureRise > 3 && temperatureRise <= 6) {
                HURRICANE_RATE += new Random().nextInt(6) + 10;
            } else if (temperatureRise > 6) {
                HURRICANE_RATE = 100;
                WILDFIRE_RATE = 100;
                DROUGHT_RATE = 100;
                FLOOD_RATE = 100;
                PEST_RATE = 100;
            }

            //update climate
            //make sure probabilities do not exceed 100% by using the Math.min
            climateMap.put("HURRICANE", Math.min(100, climateMap.get("HURRICANE") + HURRICANE_RATE));
            climateMap.put("WILDFIRE", Math.min(100, climateMap.get("WILDFIRE") + WILDFIRE_RATE));
            climateMap.put("DROUGHT", Math.min(100, climateMap.get("DROUGHT") + DROUGHT_RATE));
            climateMap.put("FLOOD", Math.min(100, climateMap.get("FLOOD") + FLOOD_RATE));
            climateMap.put("PEST", Math.min(100, climateMap.get("PEST") + PEST_RATE));
            //int temperature, int wildfire, int hurricane, int pest, int drought, int flood
            //send a climate update
            sendToAll(new WeatherUpdate(temperature, climateMap.get("WILDFIRE"), climateMap.get("HURRICANE"),
                    climateMap.get("PEST"), climateMap.get("DROUGHT"), climateMap.get("FLOOD")));
            temperature++;
        } else {
            System.out.println("climate change stopped");
        }
    }

    /**
     * generate the results according to probabilities and affect the squares
     * accordingly, as well as send the updates to every client
     * @param name {String} the name of the disaster
     * @param prob {int} probability of the disaster
     */
    private void disaster(String name, int prob) {
        int rand = (int) (Math.random() * 100);
        //if we hit the probabilities
        if (rand < prob) {
            for (Square s : getRandomSquares(2)) {
                if (name.equals("HURRICANE") && !s.getUsability().equals(Square.Usability.TREE) &&
                        !s.getUsability().equals(Square.Usability.TREEPROTECTED)) {
                    s.setUsability(Square.Usability.HURRICANED);
                    removePlantFromSquare(s);
                    sendToAll(new ImageUpdate(0, s.getxCoor(), s.getyCoor(), name, AnimationStrategy.HURRICANE));
                } else if (name.equals("WILDFIRE")) {
                    s.setUsability(Square.Usability.BURNED);
                    removePlantFromSquare(s);
                    sendToAll(new ImageUpdate(0, s.getxCoor(), s.getyCoor(), name, AnimationStrategy.WILDFIRE));
                } else if (name.equals("DROUGHT") && !s.getUsability().equals(Square.Usability.TREE) &&
                        !s.getUsability().equals(Square.Usability.TREEPROTECTED)) {
                    s.setUsability(Square.Usability.DROUGHTED);
                    removePlantFromSquare(s);
                    sendToAll(new ImageUpdate(0, s.getxCoor(), s.getyCoor(), name, AnimationStrategy.DROUGHT));
                } else if (name.equals("FLOOD") && !s.getUsability().equals(Square.Usability.TREE) &&
                        !s.getUsability().equals(Square.Usability.TREEPROTECTED)) {
                    s.setUsability(Square.Usability.FLOODED);
                    removePlantFromSquare(s);

                    sendToAll(new ImageUpdate(0, s.getxCoor(), s.getyCoor(), name, AnimationStrategy.FLOOD));
                } else if (name.equals("PEST")) {
                    s.setUsability(Square.Usability.PESTED);
                    removePlantFromSquare(s);
                    sendToAll(new ImageUpdate(0, s.getxCoor(), s.getyCoor(), name, AnimationStrategy.PEST));
                }
            }
        }
    }

    /**
     * remove the plant from the square object as well as from its owner
     * @param s
     */
    private void removePlantFromSquare(Square s) {
        if (s.getPlant() != null) {
            s.getPlant().getOwner().getPlants().remove(s.getPlant());
            s.setPlant(null);
        }
    }

    /**
     * fetches random squares and add them to a hashset
     * if the squares have NORMAL, TREE or TREEPROTECTED
     * states
     *
     * @param num {int} number of squares to fetch
     * @return {HashSet<Square>} the hashset of squares that pass the criteria
     */
    private HashSet<Square> getRandomSquares(int num) {
        HashSet<Square> result = new HashSet<>();
        int squaresFound = 0;
        Collections.shuffle(board);
        for (Square s : board) {
            if (s.getUsability().equals(Square.Usability.NORMAL) ||
                    s.getUsability().equals(Square.Usability.TREE) ||
                    s.getUsability().equals(Square.Usability.TREEPROTECTED)) {
                result.add(s);
                squaresFound++;
            }

            if (squaresFound == num) {
                break;
            }
        }
        return result;
    }

    /**
     * this method handles everything that requires a selection
     * it fetches all squares that can be selected and send this
     * information to client side which will render the light up
     * for selection signals, next action will be processed by
     * apply() function
     * @param playerID {int} id of the player who sends the request
     * @param itemName {String} the item that user is using
     */

    private void processUseRequest(int playerID, String itemName) {
        //seed selection
        if (itemName.equals("PATATO SEED") || itemName.equals("PUMPKIN SEED") || itemName.equals("BEANS SEED")
                || itemName.equals("RADISH SEED") || itemName.equals("SPINACH SEED") || itemName.equals("GOURD SEED")
                || itemName.equals("PLANT TREE")) {
            gameState = GameState.SELECTION;
            HashSet<Integer[]> coords = selectSquares("SEED");
            SelectionUpdate update = new SelectionUpdate(playerID, coords);
            sendToOne(playerID, update);
        } else if (itemName.equals("PESTICIDE")) {
            gameState = GameState.SELECTION;
            HashSet<Integer[]> coords = selectSquares("PESTICIDE");
            SelectionUpdate update = new SelectionUpdate(playerID, coords);
            sendToOne(playerID, update);
        } else if (itemName.equals("PLOWER")) {
            gameState = GameState.SELECTION;
            HashSet<Integer[]> coords = selectSquares("PLOWER");
            SelectionUpdate update = new SelectionUpdate(playerID, coords);
            sendToOne(playerID, update);

        } else if (itemName.equals("FERTILIZER")) {
            gameState = GameState.SELECTION;
            HashSet<Integer[]> coords = selectSquares("FERTILIZER");
            SelectionUpdate update = new SelectionUpdate(playerID, coords);
            sendToOne(playerID, update);
        } else if (itemName.equals("COLLECT")) {
            gameState = GameState.SELECTION;
            HashSet<Integer[]> coords = selectSquares("COLLECT");
            SelectionUpdate update = new SelectionUpdate(playerID, coords);
            sendToOne(playerID, update);
        } else if (itemName.equals("WAR")) {
            for (Player player : playerList) {
                //halves the food, consumption and income
                player.setFood(halve(player.getFood()));
                player.setActionIncremenent(halve(player.getActionIncremenent()));
                player.setConsumption(halve(player.getConsumption()));
                sendToAll(new StatsUpdate(player.getPlayerID(), player.getFood(), "food"));
                sendToAll(new StatsUpdate(player.getPlayerID(), player.getConsumption(), "consumption"));
                sendToAll(new StatsUpdate(player.getPlayerID(), player.getActionIncremenent(), "earning"));
            }
            String message = currentPlayer.getUsername() + " has started a war! " +
                    "Millions of people died, everyone's earning, food storage and " +
                    "consumption are reduced by half!";
            sendToAll(new EventUpdate(message));
            consumeItem(itemName, currentPlayer);
        } else if (itemName.equals("GREEN ENERGY")) {
            greenPolicyApplied = true;
            String message = currentPlayer.getUsername() + " has adopted green energy, there will" +
                    "be no temperature rise and no increase in probability of natural disasters";
            sendToAll(new EventUpdate(message));
            consumeItem(itemName, currentPlayer);
        } else if (itemName.equals("ACCEPT REFUGEES")) {
            int currentConsumption = currentPlayer.getConsumption();
            int currentActionIncrement = currentPlayer.getActionIncremenent();
            currentPlayer.setConsumption(currentPlayer.getConsumption() + 1);
            currentPlayer.setActionIncremenent(currentPlayer.getActionIncremenent() + 2);
            sendToAll(new StatsUpdate(currentPlayer.getPlayerID(), currentPlayer.getConsumption(), "consumption"));
            sendToAll(new StatsUpdate(currentPlayer.getPlayerID(), currentPlayer.getActionIncremenent(), "earning"));
            String message = currentPlayer.getUsername() + " has accepted climate change refugees, " +
                    "providing more social welfare but also increased country's income, increasing the country's " +
                    "consumption from: " + currentConsumption + " to " + currentPlayer.getConsumption()
                    + " and increased country's earning from " + currentActionIncrement + " to " +
                    currentPlayer.getActionIncremenent();
            sendToAll(new EventUpdate(message));
            consumeItem(itemName, currentPlayer);
        } else if (itemName.equals("STARVATION")) {
            int earning = currentPlayer.getActionIncremenent();
            currentPlayer.setFood(currentPlayer.getFood() + 2);
            currentPlayer.setActionIncremenent(currentPlayer.getActionIncremenent() - 1);
            sendToAll(new StatsUpdate(currentPlayer.getPlayerID(), currentPlayer.getActionIncremenent(), "earning"));
            sendToAll(new StatsUpdate(currentPlayer.getPlayerID(), currentPlayer.getFood(), "food"));
            String message = currentPlayer.getUsername() + " has used Starvation policy on the people, " +
                    "taking 2 food units away from people, leading to massive riots and deaths, the country's " +
                    "Earning from: " + earning + " to " + currentPlayer.getActionIncremenent();
            sendToAll(new EventUpdate(message));
            consumeItem(itemName, currentPlayer);
        }
    }

    /**
     * removes the item from player, removes the item from the player loot UI and
     * removes the item from player usedItems stack
     *
     * @param itemName {String} name of the item to be removed
     * @param player   {Player} the user of this item
     */
    private void consumeItem(String itemName, Player player) {
        int playerID = player.getPlayerID();
        player.getUsedItem().pop();
        player.removeItemFromPlayer(itemName);
        sendToOne(playerID, new SellUpdate(itemName, getPlayerByID(playerID).getItemsMap().get(itemName)));

    }

    /**
     * select the squares in model
     * fetch coordinates that match the selection algorithm
     *
     * @param method {String} the name of the method to determine which algorithm to use
     * @return {HashSet<Square>} the hashset of squares that pass the criteria
     */

    private HashSet<Integer[]> selectSquares(String method) {
        HashSet<Integer[]> result = new HashSet<>();
        if (method.equals("SEED")) {
            for (Square s : board) {
                if ((s.getUsability().equals(Square.Usability.NORMAL)
                        || s.getUsability().equals(Square.Usability.TREEPROTECTED)) && s.getPlant() == null) {
                    s.setSelected(true);
                    Integer[] arr = {s.getxCoor(), s.getyCoor()};
                    result.add(arr);
                }
            }
        } else if (method.equals("PESTICIDE")) {
            for (Square s : board) {
                if (s.getUsability().equals(Square.Usability.PESTED)) {
                    s.setSelected(true);
                    Integer[] arr = {s.getxCoor(), s.getyCoor()};
                    result.add(arr);
                }
            }
        } else if (method.equals("COLLECT")) {
            for (Plant p : currentPlayer.getPlants()) {
                if (p.getState() == "PLANT") {
                    Integer[] arr = {p.getSquare().getxCoor(), p.getSquare().getyCoor()};
                    p.getSquare().setSelected(true);
                    result.add(arr);
                }
            }
        } else if (method.equals("PLOWER")) {
            for (Square s : board) {
                if (s.getUsability().equals(Square.Usability.USED)) {
                    s.setSelected(true);
                    Integer[] arr = {s.getxCoor(), s.getyCoor()};
                    result.add(arr);
                }
            }

        } else if (method.equals("FERTILIZER")) {
            for (Plant p : currentPlayer.getPlants()) {
                if (p.getState() == "SEED") {
                    Integer[] arr = {p.getSquare().getxCoor(), p.getSquare().getyCoor()};
                    p.getSquare().setSelected(true);
                    result.add(arr);
                }
            }
        }
        return result;
    }

    /**
     * unselect all squares and send the signal to client side
     */
    private void unselectAllSquares() {
        for (Square s : board) {
            if (s.isSelected()) {
                s.setSelected(false);
            }
        }
        gameState = GameState.NORMAL;

    }

    /**
     * This method processes players' plants and all plants' updates
     * and send them in a loop to clients
     * some messages need to be sent to only 1 player, others need to
     * be sent to all
     * @param playerID {int} id of the player
     * @param updates {ArrayList<MessageObject>} list of update message that client will process
     */

    private void processPlayerPlants(int playerID, ArrayList<MessageObject> updates) {
        for (MessageObject m : updates) {
            if (m instanceof StatsUpdate || m instanceof ImageUpdate || m instanceof OwnerShipUpdate
                    || m instanceof PlantInfoUpdate) {
                sendToAll(m);
            } else {
                sendToOne(playerID, m);
            }
        }
        updates.clear();
    }

    /**
     * After user has selected the target square
     * apply the item functionality
     *
     * @param itemName {String} item to use
     */
    private void apply(int playerID, String itemName, Square square) {

        sendToAll("UNSELECTALL");
        gameState = GameState.NORMAL;
        if (square.isSelected()) {
            if (itemName.equals("PATATO SEED")) {
                Patato patato = new Patato(turnManager, square);
                patato.setOwner(currentPlayer);
                square.setPlant(patato);
                sendToAll(new ImageUpdate(playerID, square.getxCoor(), square.getyCoor(), itemName, AnimationStrategy.PLANTEVOLVE));
                sendToAll(new OwnerShipUpdate(currentPlayer.getPlayerID(), square.getxCoor(), square.getyCoor()));
                updatePlant(patato);
            } else if (itemName.equals("PUMPKIN SEED")) {
                Pumpkin p = new Pumpkin(turnManager, square);
                square.setPlant(p);
                p.setOwner(currentPlayer);
                sendToAll(new ImageUpdate(playerID, square.getxCoor(), square.getyCoor(), itemName, AnimationStrategy.PLANTEVOLVE));
                sendToAll(new OwnerShipUpdate(currentPlayer.getPlayerID(), square.getxCoor(), square.getyCoor()));
                updatePlant(p);
            } else if (itemName.equals("GOURD SEED")) {
                Gourd g = new Gourd(turnManager, square);
                square.setPlant(g);
                g.setOwner(currentPlayer);
                sendToAll(new ImageUpdate(playerID, square.getxCoor(), square.getyCoor(), itemName, AnimationStrategy.PLANTEVOLVE));
                sendToAll(new OwnerShipUpdate(currentPlayer.getPlayerID(), square.getxCoor(), square.getyCoor()));
                updatePlant(g);
            } else if (itemName.equals("PLANT TREE")) {
                //set the square to usability.TREE
                square.setUsability(Square.Usability.TREE);
                //fetch adjacent coordinates and set those to usability.TREEPROTECTED
                ArrayList<Integer[]> adjacentCoords = getAllAdjacentCoords(square.getxCoor(), square.getyCoor());
                for (Integer[] ints : adjacentCoords) {
                    getSquare(ints[0], ints[1]).setUsability(Square.Usability.TREEPROTECTED);
                    sendToAll(new ImageUpdate(playerID, ints[0], ints[1], itemName, AnimationStrategy.TREEPROTECTED));
                }
                sendToAll(new ImageUpdate(playerID, square.getxCoor(), square.getyCoor(), itemName, AnimationStrategy.PLANTEVOLVE));
            } else if (itemName.equals("COLLECT")) {
                square.getPlant().collect();
                square.setUsability(Square.Usability.USED);
                processPlayerPlants(currentPlayer.getPlayerID(), square.getPlant().getMessageObjects());
                currentPlayer.getPlants().remove(square.getPlant());
                square.setPlant(null);
            } else if (itemName.equals("PESTICIDE")) {
                square.setUsability(Square.Usability.NORMAL);
                sendToAll(new ImageUpdate(playerID, square.getxCoor(), square.getyCoor(), "NORMAL", AnimationStrategy.PESTICIDE));
            } else if (itemName.equals("FERTILIZER")) {
                square.getPlant().setProductionAmount(square.getPlant().getProductionAmount() + 1);
                sendToAll(new ImageUpdate(playerID, square.getxCoor(), square.getyCoor(), "FERTILIZER", AnimationStrategy.FERTILIZER));
            } else if (itemName.equals("PLOWER")) {
                square.setUsability(Square.Usability.NORMAL);
                sendToAll(new ImageUpdate(playerID, square.getxCoor(), square.getyCoor(), "NORMAL", AnimationStrategy.PLOWER));
            }
            currentPlayer.removeItemFromPlayer(itemName);
            sendToOne(currentPlayer.getPlayerID(), new SellUpdate(itemName, currentPlayer.getItemsMap().get(itemName)));
        }
        unselectAllSquares();

    }

    /**
     * Get the arraylist of (x,y) coordinates that are adjacent to the
     * coordinate with (row, col) coordinate
     *
     * @param row {int} x coordinate of the root square
     * @param col {int} y coordinate of the root square
     * @return {ArrayList<Integer>}the list of adjacent coordinates
     */
    private ArrayList<Integer[]> getAllAdjacentCoords(int row, int col) {
        ArrayList<Integer[]> results = new ArrayList<Integer[]>();
        int lowerIterRow = -1;
        int upperIterRow = 1;
        int lowerIterCol = -1;
        int upperIterCol = 1;
        //setting correct boundary for iterations
        if (row == 0) {
            lowerIterRow = 0;
        }
        if (row == ROWS - 1) {
            upperIterRow = 0;
        }
        if (col == 0) {
            lowerIterCol = 0;
        }
        if (col == COLS - 1) {
            upperIterCol = 0;
        }

        for (int i = lowerIterRow; i <= upperIterRow; i++) {
            for (int j = lowerIterCol; j <= upperIterCol; j++) {
                if (i + j == 1 || i + j == -1) {
                    results.add(new Integer[]{row + i, col + j});
                }
            }
        }
        return results;
    }

    /**
     * set the current player to the other player
     * This implementation needs to be modified if one
     * intends to add more players
     */
    private void switchPlayer() {
        int currentId = currentPlayer.getPlayerID();
        currentPlayer = playerList.get(2 - currentId);
    }

    /**
     * check whether the sender of a request has the id that matches the current player
     *
     * @param id
     * @return
     */
    private boolean matchCurrentId(int id) {
        return id == currentPlayer.getPlayerID();
    }

    /**
     * get a player from her id
     * @param id {int} id of the search
     * @return {Player} the player that has the id
     */
    private Player getPlayerByID(int id) {
        for (Player player : playerList) {
            if (player.getPlayerID() == id) {
                return player;
            }
        }
        return null;
    }

    /**
     * send the plant maturity info to every client
     *
     * @param p {Plant} the Plant to update info on
     */
    private void updatePlant(Plant p) {
        PlantInfoUpdate update = new PlantInfoUpdate(p.getOwner().getPlayerID(), p.getSquare().getxCoor(),
                p.getSquare().getyCoor(), p.getTurnToMaturity(), p.getTurnManager().getTurn() -
                p.getBornTurnNumber());
        sendToAll(update);
    }

    /**
     * halves an integer, returns the division result if the number is even
     * if the number is odd, do the integer division and add 1 to it to
     * prevent the number from reaching 0. eg: halve(5) = 5/2 +1 = 2+1 = 3
     * @param num {int} the number to be halved
     * @return {int} the halved result
     */

    private int halve(int num){
        if(num%2==0){
            return num/2;
        }

        else{
            return num/2+1;
        }
    }

    /**
     * clean up all resources and reset them to initial values and states
     * for the next game that clients might create
     */
    private void cleanUpResources(){
        num_connected_players = 0;
        gameState = GameState.NORMAL;
        playerList = new ArrayList<>();
        currentPlayer = new Player(0, "");
        turnManager = new TurnManager();
        board = new ArrayList<>();
        temperature = 15;
        HURRICANE_RATE = 0;
        PEST_RATE = 0;
        WILDFIRE_RATE = 0;
        FLOOD_RATE = 0;
        DROUGHT_RATE = 0;
        greenPolicyApplied = false;
    }

}

