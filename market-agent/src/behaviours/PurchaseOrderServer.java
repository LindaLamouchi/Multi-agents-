package behaviours;


import agents.SellerAgent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import gui.SellerGui;

public class PurchaseOrderServer extends CyclicBehaviour{
  
  SellerAgent bsAgent;
  SellerGui gui;
  
  public PurchaseOrderServer(SellerAgent a, SellerGui g) {
    bsAgent = a;
    gui = g;
  }
  
  public void action() {
    MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
    ACLMessage msg = bsAgent.receive(mt);
    
    if(msg != null) {
      String title = msg.getContent();
      ACLMessage reply = msg.createReply();
      
      Integer price = (Integer)bsAgent.getCatalogue().remove(title);
      if(price != null) {
        reply.setPerformative(ACLMessage.INFORM);
        
        //Generate the string for confirmation
        String response = title + " sold to agent " + msg.getSender().getName();
        gui.setSoldField(response); //Update the GUI for the seller
        System.out.println(response); //Confirm the sold on console
      } else {
        reply.setPerformative(ACLMessage.FAILURE);
        reply.setContent("not-available");
      }
      bsAgent.send(reply);
    } else {
      block();
    }
  }
}