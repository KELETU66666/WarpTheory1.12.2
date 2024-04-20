package shukaro.warptheory.util;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class ChatHelper
{
    private static Random rand = new Random();

    public static void sendToPlayer(EntityPlayer player, String message)
    {
        player.sendMessage(new TextComponentString(message));
    }

    public static String getUsername(ITextComponent message)
    {
        if (!message.getUnformattedText().contains("<") || !message.getUnformattedText().contains(">"))
            return "";
        return message.getUnformattedText().split(" ")[0].replace("<", "").replace(">", "");
    }

    @SideOnly(Side.CLIENT)
    public static String getFormattedUsername(ITextComponent message)
    {
        if (!message.getFormattedText().contains("<") || !message.getFormattedText().contains(">"))
            return "";
        return message.getFormattedText().split(" ")[0];
    }

    public static String getText(ITextComponent message)
    {
        return message.getUnformattedText().replaceFirst("<.*> ", "");
    }

    @SideOnly(Side.CLIENT)
    public static String getFormattedText(ITextComponent message)
    {
        return message.getFormattedText().replaceFirst("<.*> ", "");
    }

    public static String garbleMessage(ITextComponent message)
    {
        String text = getText(message);
        String newText = "";
        for (String word : text.split("\\s+"))
        {
            String newWord = "";
            for (int i = 0; i < word.length(); i++)
            {
                String c;
                if (word.charAt(i) == '\u00A7')
                {
                    c = String.valueOf(word.charAt(i) + String.valueOf(word.charAt(i + 1)));
                    i++;
                }
                else
                    c = rand.nextInt(5) == 0 ? FormatCodes.RandomChar.code + String.valueOf(word.charAt(i)) + FormatCodes.Reset.code : String.valueOf(word.charAt(i));
                if (rand.nextBoolean())
                    newWord = newWord + c;
                else
                    newWord = c + newWord;
            }
            if (rand.nextBoolean())
                newText = newText + (newWord.length() > 0 && newText.length() > 0 ? " " : "") + newWord;
            else
                newText = newWord + (newWord.length() > 0 && newText.length() > 0 ? " " : "") + newText;
        }
        return FormatCodes.Reset.code + newText + FormatCodes.Reset.code;
    }
}
