import javax.swing.text.*;

/**
 * https://stackoverflow.com/questions/3519151/how-to-limit-the-number-of-characters-in-jtextfield
 */
public class JTextFieldLimit extends PlainDocument {
   private int limit;

   public JTextFieldLimit(int limit)
   {
      this.limit = limit;
   }

   public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException
   {
      if (str == null)
         return;
      if ((getLength() + str.length()) <= limit) {
         super.insertString(offset, str, attr);
      }
   }
}