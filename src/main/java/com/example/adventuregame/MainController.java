package com.example.adventuregame;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private TextField hitPoint, strength, dexterity, intelligence, totalGold;

    @FXML
    private Label roomName;

    private Room[][] rooms = new Room[10][10];

    private Random rand = new Random();

    private Player player = new Player();

    private int roomX, roomY;


    public MainController() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int probabilityOfBlocked = rand.nextInt(100);

                rooms[i][j] = new Room();

                rooms[i][j].IsBlocked = (probabilityOfBlocked % 4) == 0;

                rooms[i][j].gold = rand.nextInt(10);

                int probabilityOfMonster = rand.nextInt(100);

                if ((probabilityOfMonster % 2) == 0) {
                    rooms[i][j].monster = new GameCharacter();
                    rooms[i][j].monster.HitPoint = rand.nextInt(5) + 1;
                    rooms[i][j].monster.Strength = (rand.nextInt(5) + 1) * 2;
                    rooms[i][j].monster.Dexterity = (rand.nextInt(5) + 1) * 2;
                    rooms[i][j].monster.Intelligence = (rand.nextInt(5) + 1) * 2;
                } else {
                    rooms[i][j].monster = null;
                }
            }
        }

        player = new Player();
        player.HitPoint = 20;
        player.Strength = ((rand.nextInt(5) + 1)) + ((rand.nextInt(5) + 1)) + ((rand.nextInt(5) + 1));
        player.Dexterity = ((rand.nextInt(5) + 1)) + ((rand.nextInt(5) + 1)) + ((rand.nextInt(5) + 1));
        player.Intelligence = ((rand.nextInt(5) + 1)) + ((rand.nextInt(5) + 1)) + ((rand.nextInt(5) + 1));
        player.TotalGold = 0;

        rooms[0][0].IsBlocked = false;
        roomX = 0;
        roomY = 0;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hitPoint.setText(String.format("%d", player.HitPoint));
        strength.setText(String.format("%d", player.Strength));
        dexterity.setText(String.format("%d", player.Dexterity));
        intelligence.setText(String.format("%d", player.Intelligence));
        totalGold.setText(String.format("%d", player.TotalGold));

        roomName.setText(String.format("Room %d_%d", roomX + 1, roomY + 1));
    }

    @FXML
    protected void onSearchingRoomButtonClick() {

        if (rooms[roomX][roomY].monster != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Kill the monster before do any action !!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        int roll = rand.nextInt(19) + 1;

        if (roll < player.Intelligence) {

            player.TotalGold += rooms[roomX][roomY].gold;
            totalGold.setText(String.format("%d", player.TotalGold));

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Found some gold !! (" + rooms[roomX][roomY].gold + " gold coins)", ButtonType.OK);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Couldn`t find any gold !!", ButtonType.OK);
            alert.showAndWait();
        }
        return;
    }

    @FXML
    protected void onFightButtonClick() {

        if (rooms[roomX][roomY].monster == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "There is no monster to attack !!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        int roll = rand.nextInt(19) + 1;

        if (roll >= rooms[roomX][roomY].monster.Dexterity) { //We hit
            if ((player.Strength / 3) <= 0)
                rooms[roomX][roomY].monster.HitPoint -= 1;
            else
                rooms[roomX][roomY].monster.HitPoint -= player.Strength / 3;
        }

        if (rooms[roomX][roomY].monster.HitPoint > 0) {
            if ((rooms[roomX][roomY].monster.Strength / 3) <= 0)
                player.HitPoint -= 1;
            else
                player.HitPoint -= rooms[roomX][roomY].monster.Strength / 3;
        }

        hitPoint.setText(String.format("%d", player.HitPoint));

        if (player.HitPoint <= 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "You are dead !!", ButtonType.OK);
            alert.showAndWait();
            System.exit(0);
        }
    }

    @FXML
    protected void onRunAwayButtonClick() {

        int roll = rand.nextInt(19) + 1;

        if (rooms[roomX][roomY].monster != null && rooms[roomX][roomY].monster.Intelligence > roll) {
            if ((rooms[roomX][roomY].monster.Strength / 3) <= 0)
                player.HitPoint -= 1;
            else
                player.HitPoint -= rooms[roomX][roomY].monster.Strength / 3;

            hitPoint.setText(String.format("%d", player.HitPoint));

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "You got attacked by a monster !!", ButtonType.OK);
            alert.showAndWait();
        }

        if (player.HitPoint <= 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "You are dead !!", ButtonType.OK);
            alert.showAndWait();
            System.exit(0);
        } else {
            rooms[roomX][roomY].monster = null;
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Monster can't see you run to other room !!", ButtonType.OK);
            alert.showAndWait();
        }

    }

    @FXML
    protected void onSleepButtonClick() {
        if (rooms[roomX][roomY].monster != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Monster is in the room !!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        int roll = rand.nextInt(100);

        if ((roll % 6) == 0) {
            rooms[roomX][roomY].monster = new GameCharacter();
            rooms[roomX][roomY].monster.HitPoint = rand.nextInt(5) + 1;
            rooms[roomX][roomY].monster.Strength = (rand.nextInt(5) + 1) * 2;
            rooms[roomX][roomY].monster.Dexterity = (rand.nextInt(5) + 1) * 2;
            rooms[roomX][roomY].monster.Intelligence = (rand.nextInt(5) + 1) * 2;


            Alert alert = new Alert(Alert.AlertType.INFORMATION, "A monster found in this room !!", ButtonType.OK);
            alert.showAndWait();
        } else {
            player.HitPoint = 20;

            hitPoint.setText(String.format("%d", player.HitPoint));

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "You have regained your hit points !!", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    protected void onNorthButtonClick() {

        if (rooms[roomX][roomY].monster != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Kill the monster before you leave the room !!", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        if (roomX == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Dead End !!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        if (rooms[roomX - 1][roomY].IsBlocked) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "The Room is blocked!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        roomX--;
        roomName.setText(String.format("Room %d_%d", roomX + 1, roomY + 1));
    }


    @FXML
    protected void onNorthEastButtonClick() {
        if (rooms[roomX][roomY].monster != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Kill the monster before you leave the room !!", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        if (roomX == 0 || roomY == 9) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Dead End !!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        if (rooms[roomX - 1][roomY + 1].IsBlocked) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "The Room is blocked!", ButtonType.OK);
            alert.showAndWait();

            return;
        }

        roomX--;
        roomY++;
        roomName.setText(String.format("Room %d_%d", roomX + 1, roomY + 1));
    }

    @FXML
    protected void onNorthWestButtonClick() {
        if (rooms[roomX][roomY].monster != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Kill the monster before you leave the room !!", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        if (roomX == 0 || roomY == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Dead End !!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        if (rooms[roomX - 1][roomY - 1].IsBlocked) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "The Room is blocked!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        roomX--;
        roomY--;
        roomName.setText(String.format("Room %d_%d", roomX + 1, roomY + 1));
    }

    @FXML
    protected void onSouthButtonClick() {
        if(rooms[roomX][roomY].monster != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Kill the monster before you leave the room !!", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        if(roomX == 9) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Dead End !!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        if(rooms[roomX + 1][roomY].IsBlocked) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "The Room is blocked!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        roomX++;
        roomName.setText(String.format("Room %d_%d", roomX + 1, roomY + 1));
    }

    @FXML
    protected void onSouthEastButtonClick() {
        if(rooms[roomX][roomY].monster != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Kill the monster before you leave the room !!", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        if(roomX == 9 || roomY == 9) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Dead End !!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        if(rooms[roomX + 1][roomY + 1].IsBlocked) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "The Room is blocked!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        roomX++;
        roomY++;
        roomName.setText(String.format("Room %d_%d", roomX + 1, roomY + 1));
    }

    @FXML
    protected void onSouthWestButtonClick() {
        if(rooms[roomX][roomY].monster != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Kill the monster before you leave the room !!", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        if(roomX == 9 || roomY == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Dead End !!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        if(rooms[roomX + 1][roomY - 1].IsBlocked) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "The Room is blocked!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        roomX++;
        roomY--;
        roomName.setText(String.format("Room %d_%d", roomX + 1, roomY + 1));
    }

    @FXML
    protected void onWestButtonClick() {
        if(rooms[roomX][roomY].monster != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Kill the monster before you leave the room !!", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        if(roomY == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Dead End !!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        if(rooms[roomX][roomY - 1].IsBlocked) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "The Room is blocked!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        roomY--;
        roomName.setText(String.format("Room %d_%d", roomX + 1, roomY + 1));
    }

    @FXML
    protected void onEastButtonClick() {
        if(rooms[roomX][roomY].monster != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Kill the monster before you leave the room !!", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        if(roomY == 9) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Dead End !!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        if(rooms[roomX][roomY + 1].IsBlocked) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "The Room is blocked!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        roomY++;
        roomName.setText(String.format("Room %d_%d", roomX + 1, roomY + 1));
    }
}