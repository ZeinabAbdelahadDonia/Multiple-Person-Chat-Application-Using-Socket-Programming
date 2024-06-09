# Multiple-Person-Chat-Application-Using-Socket-Programming

## Description

This Chat Application uses socket programming to allow multiple connected nodes to communicate following a hybrid p2p ring topology and a centralized node.

## System Components

The system consists of 4 connected nodes sharing messages representing friends in a group chat and a central coordinator node with a distinctive administrative role.

## System Design

All the friends, client nodes, are connected to each other forming a ring. The coordinator is responsible for assigning the client nodes their position in the ring and updating the overall structure. 

The coordinator has an ArrayList of all the members in the system to determine their position. The ArrayList was implemented to serve as a circular linked list to maintain connections between nodes. When a node is joining or leaving the system the coordinator updates the ArrayList to maintain the circular form. After every update, the coordinator sends each client node its successor so that each node always knows its updated successor.

The coordinator is also responsible for sending public messages between the nodes. If a client node wishes to send a message to everyone in the system, it simply sends it to the coordinator. The coordinator has a record of everyone in the system and would then send the message to all other nodes. 

When the client nodes want to send a private message, it sends an encrypted message to its successor in the ring until the message reaches the wanted receiver. If the message reaches the designated recipient, it will be decrypted and shown to the recipient only, if not, it will simply be passed around the ring. Hence, the circular linked list implementation using and ArrayList allows the messages to be communicated through a unidirectional circular track, each node only extracts and “knows” the port number of its successor in the ring, and messages are passed clockwise between the nodes.

To meet the logical structure and functionality of the system, two additional classes were developed throughout the implementation process: the Message and Member classes. Both classes were instantiated and sent constantly between nodes. The Member class was used to represent the nodes in the system and allow for the circular linked list implementation. The Message class was used to represent the public and private messages sent between nodes and requests sent to the coordinator: joining, leaving, and sending public messages.


## System Architecture

The communication between the nodes and the coordinator is simply a client server model where the clients request joining, leaving, or sending public messages, and the coordinator would provide these services to the clients. The network structure of the coordinator and the nodes alone is a centralized network, where all the nodes are connected to a centralized multithreaded server, the coordinator. 

Each client node is composed of a client and a multithreaded server. The client part is responsible for sending messages and requests to the coordinator and its neighbor node. The multithreaded server receives messages from the coordinator and other nodes. Both components are connected to represent the same entity, the clients. 

The circular network established between the client nodes represents a P2P architecture where nodes send and receive messages acting as clients and servers simultaneously. This decentralized architecture allows direct messaging between the nodes without the interference of a centralized organizer.

Overall, the architecture of the network is a hybrid architecture combining both P2P and client-server features.

## Advantages and Disadvantages of the System

•	Scalability: 

  o	Disadvantages: the system’s architecture significantly undermines scalability since the coordinator server is working alone to support and provide services to all the nodes. Increasing the number of nodes would cause the server to go down since its load capacity won’t be able to match the demands of the nodes.
  
  o	Advantages: private messages are exclusively sent between nodes without the interference of the coordinator, so even though the client-server architecture between nodes and the coordinator is not scalable, the p2p connection between the nodes provide the system with better scalability than a pure client-server model.

•	Fault tolerance: 

  o	Disadvantages: the client-server network between the coordinator and the nodes is tremendously fault intolerant. If the server were to go down, all the clients wouldn’t be able to join, leave, or send public messages. That would cause disruptions to the system and entirely stop the operability of the latter functions. Since the system is implemented using a ring topology, the failure of one client node would obstruct any connection between other functioning surrounding nodes. In that case, the failure of one node would halt private messaging, especially since the messages are passed around the ring in a single direction.
  
  o	Advantages: If all client nodes are functioning and in a constant state (no updates in the ArrayList required), the p2p connections between the nodes allow for a possible way of communication even when the server is down. The system wouldn’t completely crash. 

•	Consistency:

  o	Advantages: the information is updated in all related parts of the system when a change is made, making sure all nodes in the system have the current and most recent information. For example, when a new client joins or leaves the chat, the ArrayList is updated, and all the nodes are familiarized with their new neighbor nodes automatically.
  
  o	Disadvantages: the process of updating the nodes with the new information is not atomic, so if any problem were to happen mid-operation, the client nodes won’t have consistent data because some nodes will be updated, and others will still hold old information. That will cause disruptions in the system since it will entirely ruin the circular architecture of the nodes. 

•	Load Balancing: 

  o	Disadvantages: the coordinator is responsible entirely for allowing nodes to join, leave, and send public messages which is not a good design for sharing the load in the system. The load balance is not great and because of that, the system provides bad fault tolerance. 
  
  o	Advantages: the p2p private messaging provides a more balanced aspect than the centralized architecture. The nodes all share the load of handling private messages, and there is imbalance between the workload of the synonymous nodes. 

•	Replication: 

  o	Disadvantages: The ArrayList carrying the members currently in the chat is not replicated in another server or any of the other nodes, so that worsens the availability of the system. 





