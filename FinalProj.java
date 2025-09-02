
import java.util.Scanner;
import static java.lang.System.exit;

public class FinalProj {
    public static final int max_vehicles = 100;
    public static final int max_users = 100, max_transaction = 100; // MAXIMUMS
    public static String[] user_id = new String[max_users];
    public static String[] pass = new String[max_users];
    public static String[] first_name = new String[max_users], last_name = new String[max_users], department = new String[max_users];
    public static char[] user_gender = new char[max_users];
    public static int[] user_age = new int[max_users], user_money = new int[max_users], admin_money = new int[max_users];
    public static int userIndex = 0;

    // VEHICLE INFORMATIONS
    public static int[][] vehicle_type = new int[max_users][max_vehicles];
    public static String[][] brand = new String[max_users][max_vehicles], color = new String[max_users][max_vehicles], sticker_number = new String[max_users][max_vehicles];
    public static int[] vehiclectr = new int[max_users];
    public static boolean[][] vehicle_status = new boolean[max_users][max_vehicles];

    // PARKING SLOTS
    public static boolean[] parkingslot1 = new boolean[10];
    public static boolean[] parkingslot2 = new boolean[10];
    public static int userctr = 0, opt = 0, vehicle_opt = 0, max_slot = 10, attempts = 0;
    public static int[][] user_selectSlot = new int[max_users][max_slot];
    public static int parkingslot_price = 15;
    public static int slotNumber = -1;

    // Transaction
    public static int[] trans_number = new int[max_transaction];
    public static int[] admin_transnumber = new int[max_transaction];
    public static int[] all_trans = new int[max_transaction];
    public static String[] a_first_name = new String[max_transaction];

    // ADMIN History - not used in current arrays but kept for future
    public static int[][] admin_history = new int[max_transaction][];

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        fixeduser(); // pre-load sample users
        mainmenu(s);
        s.close();
    }

    // Preload users & vehicles
    public static void fixeduser() {
        // User 1
        user_id[userctr] = "202310604";
        pass[userctr] = "1";
        first_name[userctr] = "Zion";
        last_name[userctr] = "Del Rosario";
        department[userctr] = "DCS";
        user_gender[userctr] = 'M';
        user_age[userctr] = 21;
        user_money[userctr] = 500;
        // init selectSlot to -1 for safety
        for (int i = 0; i < max_slot; i++) user_selectSlot[userctr][i] = -1;
        int index = vehiclectr[0];
        sticker_number[0][index] = "ABC-1";
        vehicle_type[0][index] = 1;
        brand[0][index] = "Honda";
        color[0][index] = "Blue";
        vehicle_status[0][index] = true;
        vehiclectr[0]++;
        userctr++;

        // User 2
        user_id[userctr] = "202310570";
        pass[userctr] = "2";
        first_name[userctr] = "Mark Andrei";
        last_name[userctr] = "Abueg";
        department[userctr] = "DCS";
        user_gender[userctr] = 'M';
        user_age[userctr] = 20;
        user_money[userctr] = 500;
        for (int i = 0; i < max_slot; i++) user_selectSlot[userctr][i] = -1;
        index = vehiclectr[1];
        sticker_number[1][index] = "ABC-2";
        vehicle_type[1][index] = 1;
        brand[1][index] = "Kawasaki";
        color[1][index] = "Black";
        vehicle_status[1][index] = true;
        vehiclectr[1]++;
        userctr++;
    }

    public static void mainmenu(Scanner s) {
        while (true) {
            System.out.println("\nCVSU-CCAT PARKING SYSTEM\n");
            System.out.println("[1] REGISTER");
            System.out.println("[2] LOGIN");
            System.out.println("[3] EXIT");
            System.out.print("Opt: ");
            String in = s.nextLine().trim();
            if (in.isEmpty()) continue;
            switch (in) {
                case "1": register(s); break;
                case "2": login(s); break;
                case "3": System.out.println("Exiting..."); return;
                default: System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public static void login(Scanner s) {
        System.out.println("Attempts: " + attempts);
        System.out.print("\nLOG IN\nUser ID: ");
        String username = s.nextLine().trim();
        if (username.equalsIgnoreCase("admin")) {
            System.out.print("Enter Admin Password: ");
            String password = s.nextLine();
            if (password.equals("admin")) {
                attempts = 0;
                System.out.println("Welcome Admin\n");
                adminMenu(s);
                return;
            } else {
                attempts++;
                System.out.println("Invalid Admin Password, please try again.");
                if (attempts >= 5) {
                    System.out.println("Too many failed attempts. Exiting...");
                    exit(0);
                }
                return;
            }
        }

        int idx = userFind(username);
        if (idx == -1) {
            attempts++;
            System.out.println("Username not found. Please register first");
            if (attempts >= 5) {
                System.out.println("Too many failed attempts. Exiting...");
                exit(0);
            }
            return;
        }

        // Ask for password, allow attempts but don't recurse
        for (int i = 0; i < 5; i++) {
            System.out.print("Enter password: ");
            String password = s.nextLine();
            if (pass[idx] != null && pass[idx].equals(password)) {
                System.out.println("Log in successfully!\n");
                attempts = 0; // reset attempts after success
                usermenu(s, idx);
                return;
            } else {
                System.out.println("Password Incorrect please try again");
                attempts++;
                if (attempts >= 5) {
                    System.out.println("Too many failed attempts. Exiting...");
                    exit(0);
                }
            }
        }
    }

    public static void register(Scanner s) {
        if (userctr >= max_users) {
            System.out.println("User limit reached, cannot register more users");
            return;
        }
        System.out.println("\nREGISTER\n");
        System.out.print("User ID: ");
        String username = s.nextLine().trim();
        if (username.isEmpty()) {
            System.out.println("Username cannot be empty.");
            return;
        }
        if (isUsernameTaken(username)) {
            System.out.println("Username already exists, please try again");
            return;
        }

        System.out.print("Enter password: ");
        String password = s.nextLine();
        System.out.print("Enter First Name: ");
        String fname = s.nextLine();
        System.out.print("Enter Last Name: ");
        String lname = s.nextLine();
        System.out.print("Enter Department: ");
        String dept = s.nextLine();
        System.out.print("Enter Gender (M/F): ");
        String g = s.nextLine();
        char gender = (g.isEmpty() ? 'U' : g.charAt(0));
        System.out.print("Enter Age: ");
        int age;
        try {
            age = Integer.parseInt(s.nextLine().trim());
        } catch (Exception e) {
            System.out.println("Invalid age. Registration cancelled.");
            return;
        }

        // store
        user_id[userctr] = username;
        pass[userctr] = password;
        first_name[userctr] = fname;
        last_name[userctr] = lname;
        department[userctr] = dept;
        user_gender[userctr] = gender;
        user_age[userctr] = age;
        user_money[userctr] = 0;
        for (int i = 0; i < max_slot; i++) user_selectSlot[userctr][i] = -1;
        vehiclectr[userctr] = 0;
        trans_number[userctr] = 0;
        admin_transnumber[userctr] = 0;
        userctr++;
        System.out.println("Registered Successfully!");
    }

    private static boolean isUsernameTaken(String user) {
        for (int i = 0; i < userctr; i++) {
            if (user_id[i] != null && user_id[i].equals(user)) {
                return true;
            }
        }
        return false;
    }

    private static int userFind(String user) {
        for (int i = 0; i < userctr; i++) {
            if (user_id[i] != null && user_id[i].equals(user)) {
                return i;
            }
        }
        return -1;
    }

    private static int vehicleFind(String p_number) {
        if (p_number == null) return -1;
        for (int u = 0; u < userctr; u++) {
            for (int i = 0; i < vehiclectr[u]; i++) {
                if (sticker_number[u][i] != null && sticker_number[u][i].equals(p_number)) {
                    return u;
                }
            }
        }
        return -1;
    }

    public static void usermenu(Scanner s, int userIndex) {
        while (true) {
            System.out.println("\nUSER MENU\n");
            System.out.print("[1] Profile\n[2] Book\n[3] Transaction\n[4] Vehicles\n[5] Cash in\n[6] Log out\nOpt: ");
            String choice = s.nextLine().trim();
            switch (choice) {
                case "1": profile(s, userIndex); break;
                case "2": book(s, userIndex); break;
                case "3": transaction(s, userIndex); break;
                case "4": vehicle(s, userIndex); break;
                case "5": cash_in(s, userIndex); break;
                case "6":
                    System.out.println("Logging out...");
                    return;
                default: System.out.println("Invalid input, please try again.");
            }
        }
    }

    public static void profile(Scanner s, int userIndex) {
        System.out.println("---------------------------------------\n\t\tPROFILE\t\t\t\n---------------------------------------");
        System.out.println("User Index: " + userIndex);
        System.out.println("First Name: " + first_name[userIndex]);
        System.out.println("Last Name:  " + last_name[userIndex]);
        System.out.println("ID:         " + user_id[userIndex]);
        System.out.println("Department: " + department[userIndex]);
        System.out.println("Gender:     " + user_gender[userIndex]);
        System.out.println("Age:        " + user_age[userIndex]);
        System.out.println("Vehicles:   " + vehiclectr[userIndex]);
        System.out.println("User Money: " + user_money[userIndex]);
        System.out.println("---------------------------------------");
        // return to usermenu loop
    }

    // BOOK
    public static void book(Scanner s, int userIndex) {
        System.out.println("---------------------------------------");
        System.out.println("\t\tBOOK");
        System.out.println("---------------------------------------");

        if (vehiclectr[userIndex] <= 0) {
            System.out.println("No vehicle available, please register first.");
            return;
        }

        // DISPLAY USER'S VEHICLE
        for (int i = 0; i < vehiclectr[userIndex]; i++) {
            System.out.print("No.:         " + i + "\nSticker No.: " + sticker_number[userIndex][i] + "\nClass:       " + vehicle_type[userIndex][i] + "\nBrand:       " + brand[userIndex][i] + "\nColor:       " + color[userIndex][i] + "\nStatus:      ");
            System.out.println(vehicle_status[userIndex][i] ? "Available" : "Booked");
            if (!vehicle_status[userIndex][i]) {
                int sslot = user_selectSlot[userIndex][i];
                if (sslot >= 0) System.out.println("Slot No. " + sslot);
            }
            System.out.println("---------------------------------------");
        }

        System.out.print("\nSELECT NO.\nOption: ");
        int sel;
        try {
            sel = Integer.parseInt(s.nextLine().trim());
        } catch (Exception e) {
            System.out.println("Invalid input.");
            return;
        }

        if (sel < 0 || sel >= vehiclectr[userIndex]) {
            System.out.println("Invalid vehicle number.");
            return;
        }
        vehicle_opt = sel;

        if (!vehicle_status[userIndex][vehicle_opt]) {
            System.out.print("Do you want to unbook vehicle " + brand[userIndex][vehicle_opt] + "\n[1] Yes  [0] No\nOpt: ");
            String o = s.nextLine().trim();
            if (o.equals("1")) {
                int slotUnbook = user_selectSlot[userIndex][vehicle_opt];
                if (slotUnbook >= 0) {
                    if (vehicle_type[userIndex][vehicle_opt] == 1) {
                        if (slotUnbook >= 0 && slotUnbook < parkingslot1.length) parkingslot1[slotUnbook] = false;
                    } else if (vehicle_type[userIndex][vehicle_opt] == 2) {
                        if (slotUnbook >= 0 && slotUnbook < parkingslot2.length) parkingslot2[slotUnbook] = false;
                    }
                }
                vehicle_status[userIndex][vehicle_opt] = true;
                System.out.println("Vehicle unbooked successfully, and parking slot is now available.");
                if (trans_number[userIndex] > 0) trans_number[userIndex]--;
                if (admin_transnumber[userIndex] < max_transaction) admin_transnumber[userIndex]++;
                if (all_trans[userIndex] < max_transaction) all_trans[userIndex]++;
            }
            return;
        }

        // Proceed based on vehicle type
        if (vehicle_type[userIndex][vehicle_opt] == 1) {
            class1(s, userIndex);
        } else if (vehicle_type[userIndex][vehicle_opt] == 2) {
            class2(s, userIndex);
        } else {
            System.out.println("Invalid Input!");
        }
    }

    public static void class1(Scanner s, int userIndex) {
        for (int i = 0; i < parkingslot1.length; i++) {
            if (!parkingslot1[i]) {
                System.out.println("Slot " + i + " - Available");
            } else {
                System.out.println("Slot " + i + " - Occupied");
            }
        }
        System.out.print("\nEnter the slot number to book (or -1 to cancel): ");
        int selectedSlot;
        try {
            selectedSlot = Integer.parseInt(s.nextLine().trim());
        } catch (Exception e) {
            System.out.println("Invalid input.");
            return;
        }

        if (selectedSlot == -1) {
            System.out.println("Booking canceled.");
            return;
        }
        if (selectedSlot < 0 || selectedSlot >= parkingslot1.length) {
            System.out.println("Invalid Input...");
            return;
        }

        System.out.print("\nConfirm transaction for P" + parkingslot_price + " [1] Yes [2] No\nOpt: ");
        String optStr = s.nextLine().trim();
        if (!optStr.equals("1")) {
            System.out.println("Transaction Cancelled.");
            return;
        }

        if (user_money[userIndex] < parkingslot_price) {
            System.out.println("Insufficient money, please cash in first.");
            return;
        }

        if (!parkingslot1[selectedSlot]) {
            parkingslot1[selectedSlot] = true;
            user_money[userIndex] -= parkingslot_price;
            vehicle_status[userIndex][vehicle_opt] = false;
            trans_number[userIndex]++;
            user_selectSlot[userIndex][vehicle_opt] = selectedSlot;
            System.out.println("Successfully booked in slot no. " + selectedSlot);
        } else {
            System.out.println("This slot is already occupied!");
        }
    }

    public static void class2(Scanner s, int userIndex) {
        for (int i = 0; i < parkingslot2.length; i++) {
            if (!parkingslot2[i]) {
                System.out.println("Slot " + i + " - Available");
            } else {
                System.out.println("Slot " + i + " - Occupied");
            }
        }
        System.out.print("\nEnter the slot number to book (or -1 to cancel): ");
        int selectedSlot;
        try {
            selectedSlot = Integer.parseInt(s.nextLine().trim());
        } catch (Exception e) {
            System.out.println("Invalid input.");
            return;
        }

        if (selectedSlot == -1) {
            System.out.println("Booking canceled.");
            return;
        }
        if (selectedSlot < 0 || selectedSlot >= parkingslot2.length) {
            System.out.println("Invalid Input...");
            return;
        }

        System.out.print("\nConfirm transaction for P" + parkingslot_price + " [1] Yes [2] No\nOpt: ");
        String optStr = s.nextLine().trim();
        if (!optStr.equals("1")) {
            System.out.println("Transaction Cancelled.");
            return;
        }

        if (user_money[userIndex] < parkingslot_price) {
            System.out.println("Insufficient money, please cash in first.");
            return;
        }

        if (!parkingslot2[selectedSlot]) {
            parkingslot2[selectedSlot] = true;
            user_money[userIndex] -= parkingslot_price;
            vehicle_status[userIndex][vehicle_opt] = false;
            user_selectSlot[userIndex][vehicle_opt] = selectedSlot;
            trans_number[userIndex]++;
            System.out.println("Successfully booked in slot no. " + selectedSlot);
        } else {
            System.out.println("This slot is already occupied!");
        }
    }

    public static void transaction(Scanner s, int userIndex) {
        System.out.println("TRANSACTION\n");
        System.out.print("[1] On going Transaction \n[2] Finished Transaction\nOpt: ");
        String trans_opt = s.nextLine().trim();
        switch (trans_opt) {
            case "1":
                if (trans_number[userIndex] > 0) {
                    System.out.println("Name: " + first_name[userIndex] + " " + last_name[userIndex]);
                    for (int i = 0; i < vehiclectr[userIndex]; i++) {
                        if (!vehicle_status[userIndex][i]) { // ongoing
                            System.out.println("---------------------------------------");
                            System.out.println("\tON GOING TRANSACTION");
                            System.out.println("---------------------------------------");
                            System.out.println("Sticker number: " + sticker_number[userIndex][i]);
                            System.out.println("Brand:          " + brand[userIndex][i]);
                            System.out.println("Color:          " + color[userIndex][i]);
                            System.out.println("Price:           P" + parkingslot_price);
                            System.out.println("Slot:           " + user_selectSlot[userIndex][i]);
                            System.out.println("---------------------------------------");
                        }
                    }
                } else {
                    System.out.println("No ongoing transactions.");
                }
                break;
            case "2":
                if (admin_transnumber[userIndex] > 0) {
                    System.out.println("Admin trans number: " + admin_transnumber[userIndex]);
                    System.out.println("Name: " + first_name[userIndex] + " " + last_name[userIndex]);
                    for (int i = 0; i < admin_transnumber[userIndex]; i++) {
                        System.out.println("Sticker number: " + sticker_number[userIndex][i]);
                        System.out.println("Brand:          " + brand[userIndex][i]);
                        System.out.println("Color:          " + color[userIndex][i]);
                        System.out.println("Price:           P" + parkingslot_price);
                        System.out.println("Slot:           " + user_selectSlot[userIndex][i]);
                        System.out.println("---------------------------------------");
                    }
                } else {
                    System.out.println("No Finished transactions.");
                }
                break;
            default:
                System.out.println("Invalid Input");
        }
    }

    public static void vehicle(Scanner s, int userIndex) {
        while (true) {
            System.out.println("\nVEHICLES");
            System.out.print("[1] Register Vehicle\n[2] View Vehicle\n[3] Back\nOpt: ");
            String in = s.nextLine().trim();
            switch (in) {
                case "1": regVehicle(s, userIndex); break;
                case "2": viewVehicle(s, userIndex); break;
                case "3": return;
                default: System.out.println("Invalid input, please try again.");
            }
        }
    }

    public static void regVehicle(Scanner s, int userIndex) {
        if (vehiclectr[userIndex] == max_vehicles) {
            System.out.println("You have reached maximum vehicle per user.");
            return;
        }
        System.out.println("\nVEHICLE REGISTRATION");
        System.out.print("Plate no: ");
        String p_number = s.nextLine().trim();
        if (p_number.isEmpty()) {
            System.out.println("Plate cannot be empty.");
            return;
        }
        if (vehicleFind(p_number) != -1) {
            System.out.println("Plate number is already taken.");
            return;
        }
        System.out.println("\nCLASS");
        System.out.println("[1] Two Wheels\n[2] Four Wheels");
        System.out.print("Type: ");
        int v_type;
        try {
            v_type = Integer.parseInt(s.nextLine().trim());
        } catch (Exception e) {
            System.out.println("Invalid type.");
            return;
        }
        if (v_type != 1 && v_type != 2) {
            System.out.println("Invalid vehicle class.");
            return;
        }
        System.out.print("\nBrand: ");
        String v_brand = s.nextLine();
        System.out.print("Color: ");
        String v_color = s.nextLine();

        int index = vehiclectr[userIndex];
        sticker_number[userIndex][index] = p_number;
        vehicle_type[userIndex][index] = v_type;
        brand[userIndex][index] = v_brand;
        color[userIndex][index] = v_color;
        vehicle_status[userIndex][index] = true;
        user_selectSlot[userIndex][index] = -1;
        vehiclectr[userIndex]++;
        System.out.println("\nSUCCESSFULLY REGISTERED");
        System.out.println("VEHICLE NO: " + p_number);
    }

    public static void viewVehicle(Scanner s, int userIndex) {
        System.out.println("\nVEHICLES");
        if (vehiclectr[userIndex] == 0) {
            System.out.println("No registered vehicles.");
            return;
        } else {
            for (int i = 0; i < vehiclectr[userIndex]; i++) {
                System.out.println("No.              " + i);
                System.out.println("Vehicle No:      " + sticker_number[userIndex][i]);
                System.out.println("Class:           " + vehicle_type[userIndex][i]);
                System.out.println("Brand:           " + brand[userIndex][i]);
                slotNumber = user_selectSlot[userIndex][i];
                if (slotNumber >= 0 && slotNumber < parkingslot1.length && vehicle_type[userIndex][i] == 1) {
                    System.out.println("Parking Slot No: " + slotNumber);
                } else if (slotNumber >= 0 && slotNumber < parkingslot2.length && vehicle_type[userIndex][i] == 2) {
                    System.out.println("Parking Slot No: " + slotNumber);
                }
                System.out.print("Status: ");
                System.out.println(vehicle_status[userIndex][i] ? "Available" : "Booked");
            }
        }
    }

    public static void cash_in(Scanner s, int userIndex) {
        System.out.println("USER CASH IN\t(Enter -1 to go back.)");
        System.out.print("Enter amount: ");
        String in = s.nextLine().trim();
        int cash;
        try {
            cash = Integer.parseInt(in);
        } catch (Exception e) {
            System.out.println("Invalid amount.");
            return;
        }
        if (cash == -1) {
            System.out.println("Cash in cancelled.");
            return;
        }
        if (cash <= 0) {
            System.out.println("Amount must be positive.");
            return;
        }
        admin_money[userIndex] += cash; // pending for admin confirmation
        System.out.println("\nPending Payment, wait for admin Confirmation..");
    }

    /* ---------------- ADMIN MENU & ACTIONS ---------------- */

    public static void adminMenu(Scanner s) {
        while (true) {
            System.out.println("\nADMIN\n");
            System.out.println("[1] Parking Slots");
            System.out.println("[2] History");
            System.out.println("[3] Cash In");
            System.out.println("[4] Log out");
            System.out.print("Opt: ");
            String in = s.nextLine().trim();
            switch (in) {
                case "1": admin_parkingslots(s); break;
                case "2": admin_history(s); break;
                case "3": admin_cashin(s); break;
                case "4": return;
                default: System.out.println("Invalid input. Please try again.");
            }
        }
    }

    public static void admin_parkingslots(Scanner s) {
        System.out.println("ADMIN PARKING SLOTS");
        boolean hasTransactions = false;

        for (int i = 0; i < userctr; i++) {
            if (trans_number[i] > 0) {
                hasTransactions = true;
                System.out.println("\nUser ID: " + user_id[i]);
                System.out.println("Name:    " + first_name[i] + " " + last_name[i]);
                System.out.println("----------------------------------------------");
                System.out.println("\tTransaction History");
                System.out.println("----------------------------------------------");

                for (int j = 0; j < vehiclectr[i]; j++) {
                    System.out.println("  Vehicle No: " + j);
                    if (!vehicle_status[i][j]) {
                        System.out.println("    SLOT: " + user_selectSlot[i][j]);
                        System.out.println("    Price: P" + parkingslot_price);
                    }
                    System.out.println("    Sticker Number: " + sticker_number[i][j]);
                    System.out.println("    Class:          " + vehicle_type[i][j]);
                    System.out.println("    Brand:          " + brand[i][j]);
                    System.out.println("    Color:          " + color[i][j]);
                    System.out.println("    Status:         " + (vehicle_status[i][j] ? "Available" : "Booked"));
                    System.out.println("----------------------------------------------");
                }
            }
        }
        if (!hasTransactions) {
            System.out.println("No transactions found.");
            return;
        }

        System.out.println("Enter -1 to go back.");
        while (true) {
            System.out.print("Enter User ID: ");
            String select_user = s.nextLine().trim();
            if (select_user.equals("-1")) return;
            int uIndex = userFind(select_user);
            if (uIndex == -1) {
                System.out.println("Invalid User ID. Please try again.");
                continue;
            }
            System.out.print("Select Vehicle No: ");
            int select_vehicle;
            try {
                select_vehicle = Integer.parseInt(s.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Invalid input.");
                continue;
            }
            if (select_vehicle < 0 || select_vehicle >= vehiclectr[uIndex]) {
                System.out.println("Invalid Vehicle Number. Please try again.");
                continue;
            }
            if (vehicle_status[uIndex][select_vehicle]) {
                System.out.println("Vehicle is not currently booked. No penalty can be applied.");
                continue;
            }
            System.out.println("Do you want to apply a penalty?");
            System.out.print("[1] Yes [2] No\n\nOpt: ");
            String optStr = s.nextLine().trim();
            if (optStr.equals("1")) {
                int slotUnbook = user_selectSlot[uIndex][select_vehicle];
                if (slotUnbook >= 0) {
                    if (vehicle_type[uIndex][select_vehicle] == 1 && slotUnbook < parkingslot1.length) {
                        parkingslot1[slotUnbook] = false;
                    } else if (vehicle_type[uIndex][select_vehicle] == 2 && slotUnbook < parkingslot2.length) {
                        parkingslot2[slotUnbook] = false;
                    }
                }
                vehicle_status[uIndex][select_vehicle] = true;
                user_money[uIndex] -= parkingslot_price;
                if (user_money[uIndex] < 0) user_money[uIndex] = 0; // avoid negative balance if you prefer
                if (trans_number[uIndex] > 0) trans_number[uIndex]--;
                admin_transnumber[uIndex]++;
                System.out.println("Penalty applied successfully.");
            } else {
                System.out.println("Penalty not applied.");
            }
            // ask if admin wants to continue viewing/applying
            System.out.print("Apply penalty to another user? [y/N]: ");
            String cont = s.nextLine().trim();
            if (!cont.equalsIgnoreCase("y")) return;
        }
    }

    public static void admin_cashin(Scanner s) {
        System.out.print("\nADMIN CASH IN\n");
        while (true) {
            boolean request = false;
            System.out.println("\nPending Cash-In Request:");
            System.out.println("| No. |  User ID  |              Name              | Pending Money |");
            for (int i = 0; i < userctr; i++) {
                if (admin_money[i] > 0) {
                    request = true;
                    System.out.println("   " + i + "  :  " + user_id[i] + "\t" + first_name[i] + " " + last_name[i] + "\t" + "         P:" + admin_money[i]);
                }
            }
            if (!request) {
                System.out.println("No pending cash-in requests.");
                return;
            }
            System.out.print("\nEnter User No. to confirm request, or -1 to go back.\nOpt: ");
            String in = s.nextLine().trim();
            int userIndex;
            try {
                userIndex = Integer.parseInt(in);
            } catch (Exception e) {
                System.out.println("Invalid input.");
                continue;
            }
            if (userIndex == -1) return;
            if (userIndex >= 0 && userIndex < userctr && admin_money[userIndex] > 0) {
                System.out.print("\n[1] Yes [2] No\nConfirm transaction for P" + admin_money[userIndex] + "\nUser Id: " + user_id[userIndex] + "\nName: " + first_name[userIndex] + " " + last_name[userIndex] + "\nOpt: ");
                String confirmStr = s.nextLine().trim();
                if (confirmStr.equals("1")) {
                    user_money[userIndex] += admin_money[userIndex];
                    System.out.println("\nCash in Confirmed");
                    System.out.println("User ID: " + user_id[userIndex]);
                    System.out.println("Name: " + first_name[userIndex] + " " + last_name[userIndex]);
                    System.out.println("Cash: " + admin_money[userIndex]);
                    admin_money[userIndex] = 0;
                } else {
                    System.out.println("Cash-in request denied.");
                }
            } else {
                System.out.println("Invalid user index or no pending request.");
            }
            System.out.print("Process another request? [y/N]: ");
            String cont = s.nextLine().trim();
            if (!cont.equalsIgnoreCase("y")) return;
        }
    }

    public static void admin_history(Scanner s) {
        System.out.println("ADMIN HISTORY");
        boolean hasTransactions = false;
        for (int i = 0; i < userctr; i++) {
            if (admin_transnumber[i] > 0) {
                hasTransactions = true;
                System.out.println("\nUser ID: " + user_id[i]);
                System.out.println("Name: " + first_name[i] + " " + last_name[i]);
                System.out.println("----------------------------------------------");
                System.out.println("\tTransaction History");
                System.out.println("----------------------------------------------");
                for (int j = 0; j < admin_transnumber[i]; j++) { // Show completed transactions for admin
                    System.out.println("  Transaction No:   \t" + (j + 1));
                    System.out.println("    SLOT:           \t" + user_selectSlot[i][j]);
                    System.out.println("    Sticker Number: \t" + sticker_number[i][j]);
                    System.out.println("    Class:          \t" + vehicle_type[i][j]);
                    System.out.println("    Brand:          \t" + brand[i][j]);
                    System.out.println("    Color:          \t" + color[i][j]);
                    System.out.println("    Price:           \tP" + parkingslot_price + "\n");
                    System.out.println("----------------------------------------------\n");
                }
            }
        }
        if (!hasTransactions) {
            System.out.println("No transactions found.");
        }
    }
}
