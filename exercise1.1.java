// Exercise:
// Complete the given Bidding class that will be used for a bidding system.

import java.util.ArrayList;
import java.util.stream.Collectors;

// This interface has all the agent methods you'll need to do this exercise.
// You should not implement the interface.
interface IAgent {
  // This will return the amount by which the agent wants to increase its bid by
  // (i.e. how much they want to add onto their bid as it stands so far):
  // You should only call this method once per bid. Calling it more than once for a bid
  // may result in errors.
  public int getBidIncrease();

  // a unique id you can use to identify this agent, if necessary:
  public int id();
}

public class AgentInfo {
    IAgent agent;
    int currentTotalBid;
    ArrayList<Integer> bids;

    public AgentInfo(IAgent agent, int currentTotalBid) {
        this.agent = agent;
        this.currentTotalBid =+ currentTotalBid;
        this.bids = new ArrayList<>();
        this.bids.add(currentTotalBid);
    }

    public void addBid(Integer bid) {
        bids.add(bid);
    }

    public IAgent getAgent() {
        return agent;
    }

    public void setAgent(IAgent agent) {
        this.agent = agent;
    }

    public int getCurrentTotalBid() {
        return currentTotalBid;
    }

    public void setCurrentTotalBid(int currentTotalBid) {
        this.currentTotalBid = currentTotalBid;
    }

    public ArrayList<Integer> getBids() {
        return bids;
    }

    public void setBids(ArrayList<Integer> bids) {
        this.bids = bids;
    }
}

class Bidding {
    private ArrayList<IAgent> agents;
    private ArrayList<AgentInfo> agentsInBid, agentsWithdrawnBid;
    private int heightTotalBid;
    private boolean isBidOver;

    public Bidding(ArrayList<IAgent> agents) {
        this.agents = agents;
        this.heightTotalBid = 0;
        this.isBidOver = false;
        agentsInBid = new ArrayList<>();
        agentsWithdrawnBid = new ArrayList<>();
    }

    public ArrayList<IAgent> run() {
        try{
            // for first lap
            runFirstLap();

            // for other laps
            while (!isBidOver){
                if(agentsInBid.size() < 2) isBidOver = true;
                // check if bid is over
                if(!checkCanBidContinue()) isBidOver = true;

                for(AgentInfo agentInfo : agentsInBid){
                    if(agentInfo.getCurrentTotalBid() >= heightTotalBid) continue;

                    int agentBid = agentInfo.getAgent().getBidIncrease();
                    int currentAgentBid = agentInfo.getCurrentTotalBid() + agentBid;

                    if(agentBid == 0 || currentAgentBid < heightTotalBid){
                        agentsInBid.remove(agentInfo);
                        agentsWithdrawnBid.add(agentInfo);
                        continue;
                    }

                    agentInfo.addBid(agentBid);
                    agentInfo.setCurrentTotalBid(currentAgentBid);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            return new ArrayList<IAgent>();
        }

        return (ArrayList<IAgent>) agentsInBid.stream()
                .map(a -> a.getAgent()).collect(Collectors.toList());
    }

    private boolean checkCanBidContinue(){
        boolean isContinueBid = false;
        for(AgentInfo agentInfo : agentsInBid){
            if(agentInfo.currentTotalBid < heightTotalBid){
                isContinueBid = true;
            }
        }
        return isContinueBid;
    }

    private void runFirstLap(){
        for(int i = 0; i < agents.size(); i++){
            int agentBit = agents.get(i).getBidIncrease();

            if(i != 0 && agentBit < 1) continue;
            if(agentBit > heightTotalBid) heightTotalBid = agentBit;

            agentsInBid.add(new AgentInfo(agents.get(i), agentBit));
        }
    }

}

//   Implement the sections of code marked TODO above.

//   Rules for bidding:
//   Agents always bid in sequence, e.g. agent 1 bids first, then agent 2, etc.
//   The bidding starts with the first agent deciding on their initial bid (which will be returned by `getBidIncrease`). The amount can also be 0.
//   The next agent (e.g. agent 2) must then increase their bid so that their total bid is as much as any other agent's total bid (which so far is only agent 1), or be forced to withdraw from the bidding completely. (if the agent has withdrawn, they will be skipped from here onwards). They may also decide to bid more, in which case the next agent has to match that higher total bid (or withdraw).
//   The bidding will come around to agent 1 again if their current total bid does not match the highest total bid. The agent must increase their total bid (by returning the value they want to increase it by from `getBidIncrease`) so that their total bid will match the highest total bid that any other agent has put in so far, or withdraw from the bidding. They can also have the option to decide to bid more, same as discussed above.
//   Keep in mind that the bidding can circle the group of agents more than once, depending on if agents up their bids. Each agent who wishes to proceed is required to have committed a total bid that is equal to the highest total bid made by any other agent, counted over the duration of the bidding process. NB: The bidding will end immediately once this condition is met.
//   There can be multiple agents left at the end of bidding process, all with the same bid. That will be handled by a second bidding process that's outside the scope of this exercise.

//   Hints:
// - Be sure you understand the above bidding rules clearly.
// - Do not implement a literal solution to the problem, i.e. don't read each line on its own and write code just for that part of the requirements. Rather, think about it holistically so you can come up with a solution that meets all the requirements.
// - No consideration should be paid to performance - clear, readable code is more imporant in this exercise.
