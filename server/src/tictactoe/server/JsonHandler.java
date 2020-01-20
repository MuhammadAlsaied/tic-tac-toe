package tictactoe.server;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.sql.SQLException;
import tictactoe.server.Server.User;
import tictactoe.server.db.DatabaseManager;
import tictactoe.server.models.Game;
import tictactoe.server.models.Player;

/**
 *
 * @author muhammad and Ayman Magdy
 */
public class JsonHandler {

    private DatabaseManager databaseManager;
    private final Server server;

    public JsonHandler(Server server) {
        this.server = server;
        try {
            this.databaseManager = server.getDatabaseManager();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void handle(JsonObject request, User user) {
        String requestType = request.get("type").getAsString();

        JsonObject requestData = request.getAsJsonObject("data");
        JsonObject response = null;
        switch (requestType) {
            case "signup":
                response = handleSignup(requestData, user);
                break;
            case "signin":
                response = handleSignin(requestData, user);
                break;
            case "invitation":
                response = handleInvitation(requestData, user);
                break;
            case "accept-invitation":
                response = handleInvitationAccept(requestData, user);
                break;
            case "decline-invitation":
                //{"type": "decline-invitation", "data":{"inviting_player_id": 123}}
                break;
            case "game-move":
                response = handleGameMove(request, user);
                break;
            case "multiplayer-game-end":
                response = handleGameEnd(requestData, user);
                break;
            case "chat_message":
//                response = handleMessage(requestData, user);
                break;
            case "global_chat_message":
                server.sendToAllOnlinePlayers(request);
                response = null;
                //respone = handleGlobalMessage(requestData, user);
                break;
            case "won-local-game":
                handleLocalGameWin(requestData, user);
                break;

        }

        if (response != null) {
            try {
                user.getDataOutputStream().writeUTF(response.toString());
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }

    private JsonObject handleGlobalMessage(JsonObject requestData, User user) {
        JsonObject response = new JsonObject();
        JsonObject data = new JsonObject();
        response.add("data", data);
//        while (true) {
        //String message = requestData.get("message").getAsString();
        server.sendToAllOnlinePlayers(requestData);
//        }
        return response;
    }

    private JsonObject handleSignup(JsonObject requestData, User user) {
        JsonObject response = new JsonObject();
        JsonObject data = new JsonObject();
        response.add("data", data);

        String firstName = requestData.get("firstName").getAsString();
        String lastName = requestData.get("lastName").getAsString();
        String email = requestData.get("email").getAsString();
        String password = requestData.get("password").getAsString();
        {
            try {
                Player player = null;
                try {
                    player = databaseManager.signUp(firstName, lastName, email, password);
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
                if (player != null) {
                    response.addProperty("type", "signup-success");
                    server.addNewOfflinePlayer(player);
                } else {
                    response.addProperty("type", "signup-error");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }
        return response;
    }

    private JsonObject handleSignin(JsonObject requestData, User user) {
        JsonObject response = new JsonObject();
        JsonObject data = new JsonObject();
        response.add("data", data);

        String email = requestData.get("email").getAsString();
        String password = requestData.get("password").getAsString();
        Player player = databaseManager.signIn(email, password);
        if (player == null) {
            response.addProperty("type", "signin-error");
            data.addProperty("msg", "wrong email or password");
            try {
                user.getDataOutputStream().writeUTF(response.toString());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            user.setPlayer(player);
            response.addProperty("type", "signin-success");
            data.add("my-data", player.asJson());
            try {
                user.getDataOutputStream().writeUTF(response.toString());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            server.addToOnlinePlayers(player.getId(), user);

        }
        return null;
    }

    private JsonObject handleInvitation(JsonObject requestData, User user) {
        User opponentUser = server.getOnlinePlayerById(requestData.get("invited_player_id").getAsInt());
        System.out.println(server.getSortedOnlinePlayersAsJson());
        System.out.println(server.getSortedOfflinePlayersAsJson());
        System.out.println("is oponent online" + opponentUser.getPlayer().isOnline());
        System.out.println("does oponent has game" + opponentUser.getPlayer().getCurrentGame());
        if (opponentUser.getPlayer().isOnline() && opponentUser.getPlayer().getCurrentGame() == null) {
            JsonObject response = new JsonObject();
            JsonObject data = new JsonObject();
            response.add("data", data);
            response.addProperty("type", "invitation");

            data.addProperty("inviter_player_id", user.getPlayer().getId());
            data.addProperty("inviter_player_name", user.getPlayer().getFirstName());
            try {
                opponentUser.getDataOutputStream().writeUTF(response.toString());
            } catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
        return null;
    }

    private JsonObject handleInvitationAccept(JsonObject requestData, User user) {
        User invitingPlayer = server.getOnlinePlayerById(requestData.get("inviting_player_id").getAsInt());

        if (invitingPlayer.getPlayer().isOnline() && invitingPlayer.getPlayer().getCurrentGame() == null) {
            Game game = new Game(invitingPlayer.getPlayer(), user.getPlayer());
            invitingPlayer.getPlayer().setCurrentGame(game);
            user.getPlayer().setCurrentGame(game);
            JsonObject response = new JsonObject();
            JsonObject data = new JsonObject();
            response.add("data", data);
            response.addProperty("type", "invitation-accepted");

            data.addProperty("invited_player_id", user.getPlayer().getId());
            data.addProperty("invited_player_name", user.getPlayer().getFirstName());
            try {
                invitingPlayer.getDataOutputStream().writeUTF(response.toString());
            } catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
        return null;
    }

    private JsonObject handleGameMove(JsonObject request, User user) {
        JsonObject reqData = request.get("data").getAsJsonObject();

        if (user.getPlayer().getCurrentGame() != null) {
            Game game = user.getPlayer().getCurrentGame();
            Game.Position position = Game.Position.valueOf(reqData.get("position").getAsString());
            Game.Move move = Game.Move.valueOf(reqData.get("move").getAsString());

            Player opponentPlayer = move.equals(Game.Move.X) ? game.getPlayerO() : game.getPlayerX();
            game.setNextMove(position, move);

            JsonObject response = new JsonObject();
            JsonObject data = new JsonObject();
            response.add("data", data);
            response.addProperty("type", "game-move");
            data.addProperty("position", position.toString());
            data.addProperty("move", move.toString());
            System.out.println("TEST BEFORE HANDLE MOVE TRY");
            try {
                System.out.println("TEST AFTER HANDLE MOVE TRY");
                server.getOnlinePlayerById(opponentPlayer.getId()).getDataOutputStream().writeUTF(response.toString());
                System.out.println("SERVERSIDE opponentPlayer: " + opponentPlayer);
                /*getDataOutputStream().writeUTF(response.toString()); */
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    private JsonObject handleGameEnd(JsonObject requestData, User user) {
        Player winnerPlayer = null;
        Game game = user.getPlayer().getCurrentGame();
        if (game == null) {
            return null;
        }
        int winnerId = requestData.get("winner-id").getAsInt();
        game.setWinnerId(winnerId);
        game.setGameStatus(Game.Status.finished);
        try {
            databaseManager.insertGame(game);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        Player playerX = game.getPlayerX();
        Player playerO = game.getPlayerO();
        playerX.setCurrentGame(null);
        playerO.setCurrentGame(null);
        if (playerX.getId() == winnerId) {
            playerX.incrementPoints(50);
            winnerPlayer = playerX;
        }
        if (playerO.getId() == winnerId) {
            playerO.incrementPoints(50);
            winnerPlayer = playerO;

        }
        try {
            databaseManager.updatePlayerScore(winnerPlayer);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        server.sendUpdatedPlayerList();
        return null;
    }

    private JsonObject handleLocalGameWin(JsonObject requestData, User user) {
        Player player = user.getPlayer();
        player.incrementPoints(requestData.get("added-points").getAsInt());
        try {
            databaseManager.updatePlayerScore(player);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
