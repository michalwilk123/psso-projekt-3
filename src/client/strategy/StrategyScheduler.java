package client.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import common.Item;
import server.IAuctionServer;

public class StrategyScheduler implements Runnable{
    private List<IStrategy> strategies;
    private IAuctionServer server;
    private AtomicBoolean isSchedulerStopped;

    public StrategyScheduler(IAuctionServer server) {
        this.strategies = Collections.synchronizedList(new ArrayList<>());
        this.server = server;
        this.isSchedulerStopped = new AtomicBoolean(false);
    }

    public void addStrategy(IStrategy strategy){
        synchronized (strategies) {
            if (!strategies.contains(strategy)) {
                strategies.add(strategy);
            }
        }
    }

    public void synchronizeItem(Item item){
        synchronized (strategies) {
            strategies.forEach((str) -> {
                if (str.getItem().equals(item)) {
                    str.setItem(item);
                }
            });
        }
    }

    public void removeStrategy(String strategyName){
        synchronized (strategies) {
            strategies.removeIf(s -> s.getIdentifier().equals(strategyName));
        }
    }

    public void stopScheduler(){
        this.isSchedulerStopped.set(true);
    }

    @Override
    public void run() {
        while (!isSchedulerStopped.get()) {
            synchronized (strategies) {
                strategies.forEach((it) -> {
                    if (it.isCallbackRunning()) {
                        it.runAction(server);
                    }
                });
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
