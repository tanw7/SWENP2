package mycontroller;

public class MappingObserver implements ControllerListener{
	public MappingObserver(MyAutoController controller) {
		controller.addObserver(this);
	}
	
	@Override
	public void onPropertyEvent(String source, String name, String value) {
		if(source.equals("MyAutoController") && name.equals("mapping")) {
			//logic to map into Record
		}
	}
}
