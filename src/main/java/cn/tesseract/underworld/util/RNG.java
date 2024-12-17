package cn.tesseract.underworld.util;

import net.minecraft.world.World;

import java.util.Random;

public final class RNG {
    public static final int MAX_INDEX = 32767;
    public static final int MAX_INDEX_PLUS_1 = 32768;
    public int[] int_max = new int[MAX_INDEX_PLUS_1];
    public int[] int_2 = new int[MAX_INDEX_PLUS_1];
    public int[] int_4 = new int[MAX_INDEX_PLUS_1];
    public int[] int_8 = new int[MAX_INDEX_PLUS_1];
    public int[] int_16 = new int[MAX_INDEX_PLUS_1];
    public int[] int_32 = new int[MAX_INDEX_PLUS_1];
    public int[] int_64 = new int[MAX_INDEX_PLUS_1];
    public int[] int_128 = new int[MAX_INDEX_PLUS_1];
    public int[] int_256 = new int[MAX_INDEX_PLUS_1];
    public int[] int_3 = new int[MAX_INDEX_PLUS_1];
    public int[] int_5 = new int[MAX_INDEX_PLUS_1];
    public int[] int_6 = new int[MAX_INDEX_PLUS_1];
    public int[] int_7 = new int[MAX_INDEX_PLUS_1];
    public int[] int_9 = new int[MAX_INDEX_PLUS_1];
    public int[] int_10 = new int[MAX_INDEX_PLUS_1];
    public int[] int_13 = new int[MAX_INDEX_PLUS_1];
    public int[] int_14 = new int[MAX_INDEX_PLUS_1];
    public int[] int_17 = new int[MAX_INDEX_PLUS_1];
    public int[] int_25 = new int[MAX_INDEX_PLUS_1];
    public int[] int_33 = new int[MAX_INDEX_PLUS_1];
    public int[] int_49 = new int[MAX_INDEX_PLUS_1];
    public int[] int_65 = new int[MAX_INDEX_PLUS_1];
    public int[] int_100 = new int[MAX_INDEX_PLUS_1];
    public int[] int_126 = new int[MAX_INDEX_PLUS_1];
    public int[] int_4_minus_int_4 = new int[MAX_INDEX_PLUS_1];
    public int[] int_8_minus_int_8 = new int[MAX_INDEX_PLUS_1];
    public int[] int_16_plus_8 = new int[MAX_INDEX_PLUS_1];
    public int[] int_7_minus_3 = new int[MAX_INDEX_PLUS_1];
    public int[] int_9_minus_4 = new int[MAX_INDEX_PLUS_1];
    public int[] int_13_minus_6 = new int[MAX_INDEX_PLUS_1];
    public int[] int_14_plus_1 = new int[MAX_INDEX_PLUS_1];
    public int[] int_17_minus_8 = new int[MAX_INDEX_PLUS_1];
    public int[] int_25_minus_12 = new int[MAX_INDEX_PLUS_1];
    public int[] int_33_minus_16 = new int[MAX_INDEX_PLUS_1];
    public int[] int_49_minus_24 = new int[MAX_INDEX_PLUS_1];
    public int[] int_65_minus_32 = new int[MAX_INDEX_PLUS_1];
    public int[] int_126_plus_1 = new int[MAX_INDEX_PLUS_1];
    public boolean[] chance_in_2 = new boolean[MAX_INDEX_PLUS_1];
    public boolean[] chance_in_3 = new boolean[MAX_INDEX_PLUS_1];
    public boolean[] chance_in_4 = new boolean[MAX_INDEX_PLUS_1];
    public boolean[] chance_in_6 = new boolean[MAX_INDEX_PLUS_1];
    public boolean[] chance_in_8 = new boolean[MAX_INDEX_PLUS_1];
    public boolean[] chance_in_10 = new boolean[MAX_INDEX_PLUS_1];
    public boolean[] chance_in_16 = new boolean[MAX_INDEX_PLUS_1];
    public boolean[] chance_in_32 = new boolean[MAX_INDEX_PLUS_1];
    public boolean[] chance_in_100 = new boolean[MAX_INDEX_PLUS_1];
    public float[] float_1 = new float[MAX_INDEX_PLUS_1];
    public float[] float_1_minus_float_1 = new float[MAX_INDEX_PLUS_1];
    public float[] float_1_times_float_1 = new float[MAX_INDEX_PLUS_1];
    public float[] float_2Pi = new float[MAX_INDEX_PLUS_1];
    public float[] float_1_minus_a_half_times_a_quarter = new float[MAX_INDEX_PLUS_1];
    public float[] float_2_plus_float_1 = new float[MAX_INDEX_PLUS_1];
    public double[] double_1 = new double[MAX_INDEX_PLUS_1];
    private boolean random_numbers_have_been_initialized = false;
    public int random_number_index;

    public void init(World world) {
        if (!random_numbers_have_been_initialized) {
            random_numbers_have_been_initialized = true;
            Random rand = new Random();
            rand.setSeed(world.getSeed());
            int i = -1;

            while (true) {
                ++i;
                if (i >= 32768) {
                    random_number_index = rand.nextInt();
                    return;
                }

                int int_a = rand.nextInt() & Integer.MAX_VALUE;
                int int_b = rand.nextInt() & Integer.MAX_VALUE;
                int_max[i] = int_a;
                int_2[i] = int_a & 1;
                int_4[i] = int_a & 3;
                int_8[i] = int_a & 7;
                int_16[i] = int_a & 15;
                int_32[i] = int_a & 31;
                int_64[i] = int_a & 63;
                int_128[i] = int_a & 127;
                int_256[i] = int_a & 255;
                int_3[i] = int_a % 3;
                int_5[i] = int_a % 5;
                int_6[i] = int_a % 6;
                int_7[i] = int_a % 7;
                int_9[i] = int_a % 9;
                int_10[i] = int_a % 10;
                int_13[i] = int_a % 13;
                int_14[i] = int_a % 14;
                int_17[i] = int_a % 17;
                int_25[i] = int_a % 25;
                int_33[i] = int_a % 33;
                int_49[i] = int_a % 49;
                int_65[i] = int_a % 65;
                int_100[i] = int_a % 100;
                int_126[i] = int_a % 126;
                int_4_minus_int_4[i] = int_4[i] - (int_b & 3);
                int_8_minus_int_8[i] = int_8[i] - (int_b & 7);
                int_16_plus_8[i] = int_16[i] + 8;
                int_7_minus_3[i] = int_7[i] - 3;
                int_9_minus_4[i] = int_9[i] - 4;
                int_13_minus_6[i] = int_13[i] - 6;
                int_14_plus_1[i] = int_14[i] + 1;
                int_17_minus_8[i] = int_17[i] - 8;
                int_25_minus_12[i] = int_25[i] - 12;
                int_33_minus_16[i] = int_33[i] - 16;
                int_49_minus_24[i] = int_49[i] - 24;
                int_65_minus_32[i] = int_65[i] - 32;
                int_126_plus_1[i] = int_126[i] + 1;
                chance_in_2[i] = int_2[i] == 0;
                chance_in_3[i] = int_3[i] == 0;
                chance_in_4[i] = int_4[i] == 0;
                chance_in_6[i] = int_6[i] == 0;
                chance_in_8[i] = int_8[i] == 0;
                chance_in_10[i] = int_10[i] == 0;
                chance_in_16[i] = int_16[i] == 0;
                chance_in_32[i] = int_32[i] == 0;
                chance_in_100[i] = int_100[i] == 0;
                float float_a = rand.nextFloat();
                float float_b = rand.nextFloat();
                float_1[i] = float_a;
                float_1_minus_float_1[i] = float_a - float_b;
                float_1_times_float_1[i] = float_a * float_b;
                float_2Pi[i] = (float) ((double) (float_a * 2.0F) * Math.PI);
                float_1_minus_a_half_times_a_quarter[i] = (float_a - 0.5F) * 0.25F;
                float_2_plus_float_1[i] = float_a * 2.0F + float_b;
                double double_a = rand.nextDouble();
                double_1[i] = double_a;
            }
        }
    }
}
