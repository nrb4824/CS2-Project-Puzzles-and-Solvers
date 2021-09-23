package puzzles.jam.ptui;

import puzzles.common.Observer;
import puzzles.jam.model.JamClientData;
import puzzles.jam.model.JamModel;

public class JamPTUI implements Observer<JamModel, JamClientData> {
    private JamModel model;

    @Override
    public void update(JamModel jamModel, JamClientData jamClientData) {
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java JamPTUI filename");
        }
    }
}
