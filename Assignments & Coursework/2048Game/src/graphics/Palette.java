package graphics;

import java.awt.Color;

//---------------------------------------------------------------

/**
 * Colour palette for 2048 game GUI.
 * PRA2003 assignment (2022).
 * @author cambolbro
 */
public enum Palette
{
    BACKGROUND(new Color(250, 248, 240)),  // Background colour
    FRAME(     new Color(185, 173, 161)),  // Frame colour
    TEXT(      new Color(140, 123, 104)),  // Text colour
    EMPTY(     new Color(203, 192, 181)),  // Empty cell colour
    TILE_2(    new Color(236, 228, 219)),  //     2 = 2 ^^ 1
    TILE_4(    new Color(236, 225, 204)),  //     4 = 2 ^^ 2
    TILE_8(    new Color(233, 181, 130)),  //     8 = 2 ^^ 3
    TILE_16(   new Color(233, 154, 109)),  //    16 = 2 ^^ 4
    TILE_32(   new Color(231, 131, 103)),  //    32 = 2 ^^ 5
    TILE_64(   new Color(229, 105,  72)),  //    64 = 2 ^^ 6
    TILE_128(  new Color(232, 209, 128)),  //   128 = 2 ^^ 7
    TILE_256(  new Color(232, 205, 114)),  //   256 = 2 ^^ 8
    TILE_512(  new Color(231, 202, 101)),  //   512 = 2 ^^ 9
    TILE_1024( new Color(231, 198,  89)),  //  1024 = 2 ^^ 10
    TILE_2048( new Color(230, 195,  79)),  //  2048 = 2 ^^ 11
    TILE_4096( new Color(140, 120, 100)),  //  4096 = 2 ^^ 12
    TILE_8192( new Color(120, 100,  80)),  //  8192 = 2 ^^ 13
    TILE_16384(new Color(100,  80,  60)),  // 16384 = 2 ^^ 14
    TILE_32768(new Color( 80,  60,  40)),  // 32768 = 2 ^^ 15
    TILE_65536(new Color( 60,  40,  20));  // 65536 = 2 ^^ 16

    private final Color colour;  // colour for each element

    //-------------------------------------------------------------------------

    /**
     * Private constructor.
     */
    Palette(final Color colour)
    {
        this.colour = colour;
    }

    //-------------------------------------------------------------------------

    public Color colour()
    {
        return colour;
    }

    /**
     * @param tile number, e.g. 1, 2, 4, 8, ... should be a power of 2.
     * @return Colour of the specified tile.
     */
    public static Color tileColour(final int tile)
    {
        return values()[EMPTY.ordinal() + powerOf2(tile)].colour;
    }

    //-------------------------------------------------------------------------

    /**
     * @return Highest power of 2 <= n, else 0 on bad input.
     */
    public static int powerOf2(final int n)
    {
        if (n <= 0)
            return 0;

        // Easy way to determine power of 2
        return (int)(Math.log(n) / Math.log(2));

        // Less expensive way to determine power of 2
        //int value = n;
        //int steps = 0;
        //while ((value >>= 1) != 0)
        //	steps++;
        //return steps;
    }

    //-------------------------------------------------------------------------

}
