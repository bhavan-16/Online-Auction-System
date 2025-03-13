import java.awt.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

public class Main {
    private static final ArrayList<User> teams = new ArrayList<>();
    private static final ArrayList<Item> players = new ArrayList<>();
    private static final int currentPlayerIndex = 0;
    private static Auction auction;
    private static final int TOTAL_ROUNDS = 2;
    private static int roundCounter = 0;
    private static final JLabel resultLabel = new JLabel();
    private static JButton placeBidButton;

    public static void main(String[] args) {
        JFrame frame = new JFrame("IPL Auction System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new CardLayout());

        JPanel registrationPanel = createRegistrationPanel(contentPane);
        contentPane.add(registrationPanel, "Registration");

        frame.setVisible(true);
    }

    private static JPanel createRegistrationPanel(Container contentPane) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = createGbc(5, 5, 0, 0);

        JLabel adminTitle = new JLabel("ADMIN PAGE");
        adminTitle.setFont(new Font("Arial", Font.BOLD, 24));
        addComponent(panel, adminTitle, gbc, 0, 0, 2);

        JTextField[] teamFields = new JTextField[4];
        String[] labels = {"Team 1:", "Team 2:", "Team 3:", "Team 4:"};

        for (int i = 0; i < labels.length; i++) {
            addComponent(panel, new JLabel(labels[i]), gbc, 0, i + 1);
            teamFields[i] = new JTextField(20);
            addComponent(panel, teamFields[i], gbc, 1, i + 1);
        }

        // Player Input Fields
        JLabel playerInputTitle = new JLabel("Player Details:");
        playerInputTitle.setFont(new Font("Arial", Font.BOLD, 18));
        addComponent(panel, playerInputTitle, gbc, 0, labels.length + 1, 2);

        JTextField playerNameField = new JTextField(20);
        JTextField playerRoleField = new JTextField(20);
        JTextField playerBasePriceField = new JTextField(20);

        addComponent(panel, new JLabel("Player Name:"), gbc, 0, labels.length + 2);
        addComponent(panel, playerNameField, gbc, 1, labels.length + 2);
        addComponent(panel, new JLabel("Player Role:"), gbc, 0, labels.length + 3);
        addComponent(panel, playerRoleField, gbc, 1, labels.length + 3);
        addComponent(panel, new JLabel("Base Price (in Crore):"), gbc, 0, labels.length + 4);
        addComponent(panel, playerBasePriceField, gbc, 1, labels.length + 4);

        JButton registerButton = new JButton("Register");
        addComponent(panel, registerButton, gbc, 0, labels.length + 5, 2);

        CardLayout cardLayout = (CardLayout) contentPane.getLayout();
        registerButton.addActionListener(e -> {
            registerTeams(contentPane, cardLayout, teamFields);
            registerPlayer(playerNameField, playerRoleField, playerBasePriceField);
            updateBiddingPanel(contentPane, cardLayout);
        });

        return panel;
    }

    private static void registerTeams(Container contentPane, CardLayout cardLayout, JTextField[] teamFields) {
        for (JTextField teamField : teamFields) {
            teams.add(new User(teamField.getText()));
        }
    }

    private static void registerPlayer(JTextField playerNameField, JTextField playerRoleField, JTextField playerBasePriceField) {
        String playerName = playerNameField.getText();
        String playerRole = playerRoleField.getText();
        double basePrice = Double.parseDouble(playerBasePriceField.getText());

        players.add(new Item(playerName, playerRole, basePrice));
        auction = new Auction(players.get(currentPlayerIndex));
    }

    private static void updateBiddingPanel(Container contentPane, CardLayout cardLayout) {
        JPanel biddingPanel = createBiddingPanel();
        contentPane.add(biddingPanel, "Bidding");
        cardLayout.show(contentPane, "Bidding");
    }

    private static JPanel createBiddingPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = createGbc(10, 10, 0, 0);

        String[] labels = {"Player: " + players.get(currentPlayerIndex).getName(),
                           "Role: " + players.get(currentPlayerIndex).getRole(),
                           "Starting Price: " + players.get(currentPlayerIndex).getBasePrice() + " Cr"};

        for (int i = 0; i < labels.length; i++) {
            addComponent(panel, new JLabel(labels[i]), gbc, 0, i);
        }

        JLabel[] bidLabels = new JLabel[teams.size()];
        JTextField[] bidFields = new JTextField[teams.size()];
        for (int i = 0; i < teams.size(); i++) {
            bidLabels[i] = new JLabel("Bid Amount for " + teams.get(i).getUsername() + ":");
            bidFields[i] = new JTextField(10);
            addComponent(panel, bidLabels[i], gbc, 0, labels.length + i);
            addComponent(panel, bidFields[i], gbc, 1, labels.length + i);
        }

        placeBidButton = new JButton("Place Bids");
        addComponent(panel, placeBidButton, gbc, 0, labels.length + teams.size(), 2);
        addComponent(panel, resultLabel, gbc, 0, labels.length + teams.size() + 1, 2);

        placeBidButton.addActionListener(e -> takeBids(bidFields));

        return panel;
    }

    private static void takeBids(JTextField[] bidFields) {
        try {
            boolean validBids = false;
            for (int i = 0; i < teams.size(); i++) {
                if (!bidFields[i].getText().isEmpty()) {
                    double bidAmount = Double.parseDouble(bidFields[i].getText());
                    auction.placeBid(new Bid(teams.get(i), bidAmount));
                    validBids = true;
                }
            }

            if (validBids) {
                Bid highestBid = auction.determineWinner();
                resultLabel.setText("Current Highest Bid: Team " + highestBid.getBidder().getUsername() +
                        " for ₹" + highestBid.getAmount() + " Cr! Does anyone want to bid?");
                placeBidButton.setEnabled(false);

                if (roundCounter < TOTAL_ROUNDS - 1) {
                    roundCounter++;
                    startBidTimer(bidFields);
                } else {
                    finalizeAuction(highestBid);
                }
            } else {
                resultLabel.setText("No valid bids placed.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Please enter valid bid amounts.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void startBidTimer(JTextField[] bidFields) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    resultLabel.setText("Time's up! Place your final bids.");
                    placeBidButton.setEnabled(true);
                    for (JTextField bidField : bidFields) {
                        bidField.setText("");
                    }
                });
            }
        };
        timer.schedule(task, 10000); // 10 seconds
    }

    private static void finalizeAuction(Bid highestBid) {
        resultLabel.setText("Auction final! The winner is Team " + highestBid.getBidder().getUsername() +
                " with a bid of ₹" + highestBid.getAmount() + " Cr");
        placeBidButton.setEnabled(false);
    }

    private static GridBagConstraints createGbc(int top, int left, int bottom, int right) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(top, left, bottom, right);
        return gbc;
    }

    private static void addComponent(JPanel panel, Component component, GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        panel.add(component, gbc);
    }

    private static void addComponent(JPanel panel, Component component, GridBagConstraints gbc, int x, int y, int width) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        panel.add(component, gbc);
    }
}

