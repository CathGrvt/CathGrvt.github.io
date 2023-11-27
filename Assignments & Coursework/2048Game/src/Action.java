public enum Action {
    Up,
    Right,
    Down,
    Left;
    public int drow;
    public int dcol;

    static {
        Up.drow = -1;
        Up.dcol = 0;
        Right.drow = 0;
        Right.dcol = 1;
        Down.drow = 1;
        Down.dcol = 0;
        Left.drow = 0;
        Left.dcol = -1;
    }

}
