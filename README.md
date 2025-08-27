IPL Auction System

A Java-based **IPL Auction System** built with **Swing GUI** that simulates a real-world player auction process.  
Teams can register, players can be added with base prices, and live bidding rounds determine the highest bidder.  
The system announces the winner after multiple bidding rounds.

Features
- Team Registration: Admin can register multiple teams.
- Player Registration: Admin can enter player details (name, role, and base price).
- Bidding System:
  - Each team can place bids.
  - Highest bid is tracked in real-time.
  - Multiple bidding rounds are supported.
  - Countdown timer ensures fair bidding.
- Winner Announcement: After final round, the system declares the winning team and bid price.

Tech Stack
- Language: Java (JDK 8+)
- GUI Framework: Swing (`javax.swing`, `java.awt`)
- Core Java Concepts: OOP (Classes like `User`, `Item`, `Bid`, `Auction`), Timers, Event Handling
