package mycontroller;

public class StrategyFactory {
	private IControlStrategy strategy = null;
	
	// Singleton
	private static StrategyFactory instance = new StrategyFactory();
	private StrategyFactory(){}
	
	public static StrategyFactory getInstance() {
		return instance;
	}
	
	@SuppressWarnings("unused")
	public IControlStrategy getStrategy() {
		if (true) {
			//FloodFill
		} else if(true){
			//Dijkstra
		}
		return strategy;
	}
}
