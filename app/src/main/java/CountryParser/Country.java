package CountryParser;

public class Country {
	String name;
	String flag;
	String details;
	String anthem;
	String shorty;

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShorty() {
		return shorty;
	}

	public void setShorty(String shorty) {
		this.shorty = shorty;
	}

	public Country(String name, String flag, String shorty){
		this.name = name;
		this.flag = flag;
		this.shorty=shorty;
		this.details="";

	}

	public String getAnthem() {
		return anthem;
	}

	public void setAnthem(String anthem) {
		this.anthem = anthem;
	}

	public Country() {
		// TODO Auto-generated constructor stub
	}

	public void setDetails(String details){
		this.details=details;
	}
	public String getDetails(){
		return this.details;
	}

	public int compare(Country other) {
		return  this.name.compareTo(other.name);
	}

	@Override
	public String toString() {
		return name;
	}
}
