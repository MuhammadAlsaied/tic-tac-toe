/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.server.models;

/**
 *
 * @author asoliman
 */
public class Enums {
    public static enum Status {
        inProgress, terminated, finished;
    }
    public static enum Position {
        upper_left, up, upper_right, left, center, right, lower_left, lower_right, down
    }

    public static enum Move {
        X, O
    }
}
