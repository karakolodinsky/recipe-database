import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;

public class template {
    
    public static JButton makeButton (String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Tahoma", Font.PLAIN, 15));
        return button;
    }

}
