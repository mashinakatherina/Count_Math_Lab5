
import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;

public class IntegerTextField extends JTextField {
    public IntegerTextField() {
        super();
        ((AbstractDocument) getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                if (string == null) return;
                replace(fb, offset, 0, string, attr);
            }

            @Override
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                replace(fb, offset, length, "", null);
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                fb.replace(offset, length, checkInput(text, offset), attrs);
            }

            private String checkInput(String proposedValue, int offset) {

                StringBuilder temp = new StringBuilder(getText());
                temp.insert(offset, proposedValue);
                if (temp.toString().startsWith("00")
                        || !temp.toString().matches("-?\\d*[.,]?\\d{0,16}")) {
                    Toolkit.getDefaultToolkit().beep();
                    return "";
                }
                return proposedValue.replace(",",".");
            }
        });
    }
}
