package com.bxly.toursts.enums;

public enum MapEnum {
    TestMapEnumOne("测试枚举1"),
    TestMapEnumTwo("测试枚举2"),
    TestMapEnumThree("测试枚举3"),
    TestMapEnumFour(18);

    private String testNume;
    private int testNume1;
    MapEnum(String testNume) {
        this.testNume = testNume;
    }
    MapEnum(int testNume1) {
        this.testNume1 = testNume1;
    }
    public String GetMapNumeStr() {
        return this.testNume;
    }
    public int GetMapNume() {
        return this.testNume1;
    }
    public static void main(String[] args) {
        System.out.println(MapEnum.TestMapEnumOne.testNume);
        System.out.println(MapEnum.TestMapEnumTwo.testNume);
        System.out.println(MapEnum.TestMapEnumThree.testNume);
        System.out.println(MapEnum.TestMapEnumThree.testNume1);
        System.out.println(MapEnum.TestMapEnumFour.testNume1);
        System.out.println(MapEnum.TestMapEnumFour.GetMapNume());
    }
}
