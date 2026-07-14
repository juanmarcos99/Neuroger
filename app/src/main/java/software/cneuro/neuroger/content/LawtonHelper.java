package software.cneuro.neuroger.content;

import android.content.Context;

import software.cneuro.neuroger.R;

public class LawtonHelper {
    public static String getTestTypesText(Context context, int pagePosition) {
        String[] array = context.getResources().getStringArray(R.array.lawton_tests);
        if (pagePosition < 0 || pagePosition >= array.length) {
            return "";
        }

        return array[pagePosition];
    }

    public static int getTotalItems(Context context) {
        return context.getResources().getStringArray(R.array.lawton_tests).length;
    }

    public static String[] getItemsList(Context context, int pagePosition) {
        String[] result = new String[]{};

        switch (pagePosition) {
            case 0:
                result = getPhoneItemsList(context);
                break;
            case 1:
                result = getShoppingItemsList(context);
                break;
            case 2:
                result = getFoodItemsList(context);
                break;
            case 3:
                result = getHouseItemsList(context);
                break;
            case 4:
                result = getClothingItemsList(context);
                break;
            case 5:
                result = getTransportationItemsList(context);
                break;
            case 6:
                result = getMedicationItemsList(context);
                break;
            case 7:
                result = getEconomicsItemsList(context);
                break;
        }

        return result;
    }

    private static String[] getEconomicsItemsList(Context context) {
        return context.getResources().getStringArray(R.array.lawton_finances);
    }

    private static String[] getMedicationItemsList(Context context) {
        return context.getResources().getStringArray(R.array.lawton_medication);
    }

    private static String[] getTransportationItemsList(Context context) {
        return context.getResources().getStringArray(R.array.lawton_transportation);
    }

    private static String[] getClothingItemsList(Context context) {
        return context.getResources().getStringArray(R.array.lawton_clothing);
    }

    private static String[] getHouseItemsList(Context context) {
        return context.getResources().getStringArray(R.array.lawton_house);
    }

    private static String[] getFoodItemsList(Context context) {
        return context.getResources().getStringArray(R.array.lawton_food);
    }

    private static String[] getShoppingItemsList(Context context) {
        return context.getResources().getStringArray(R.array.lawton_shopping);
    }

    private static String[] getPhoneItemsList(Context context) {
        return context.getResources().getStringArray(R.array.lawton_phone);
    }

    public static String getHelp(Context context, int pagePosition) {
        return context.getString(R.string.lawton_help);
    }

    public static String getItemsList(Context context, int pagePosition, int answerPosition) {
        String result = "";

        switch (pagePosition) {
            case 0:
                result = getPhoneItemText(context, answerPosition);
                break;
            case 1:
                result = getShoppingItemText(context, answerPosition);
                break;
            case 2:
                result = getFoodItemText(context, answerPosition);
                break;
            case 3:
                result = getHouseText(context, answerPosition);
                break;
            case 4:
                result = getClothingItemText(context, answerPosition);
                break;
            case 5:
                result = getTransportationItemText(context, answerPosition);
                break;
            case 6:
                result = getMedicationItemText(context, answerPosition);
                break;
            case 7:
                result = getEconomicsItemText(context, answerPosition);
                break;
        }

        return result;
    }

    private static String getEconomicsItemText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.lawton_finances);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    private static String getMedicationItemText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.lawton_medication);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    private static String getTransportationItemText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.lawton_transportation);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    private static String getClothingItemText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.lawton_clothing);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    private static String getHouseText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.lawton_house);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    private static String getFoodItemText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.lawton_food);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    private static String getShoppingItemText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.lawton_shopping);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

    private static String getPhoneItemText(Context context, int position) {
        String[] array = context.getResources().getStringArray(R.array.lawton_phone);
        if (position < 0 || position >= array.length) {
            return "";
        }

        return array[position];
    }

}
