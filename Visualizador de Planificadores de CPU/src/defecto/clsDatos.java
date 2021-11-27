package defecto;

public class clsDatos {
	private Object[] Info;
	
	clsDatos(Object[] Info){
		this.Info=Info;
	}
	
	public Object[] getInfo() {
		return Info;
	}

	public void setInfo(Object[] info) {
		Info = info;
	}
}
