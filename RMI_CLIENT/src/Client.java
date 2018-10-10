import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.List;
import java.util.Set;

import rental.*;

public class Client extends AbstractTestBooking {

    /********
     * MAIN *
     ********/

    public static void main(String[] args) throws Exception {

        String carRentalCompanyName = "Hertz";

        // An example reservation scenario on car rental company 'Hertz' would be...
        Client client = new Client("simpleTrips", carRentalCompanyName);
        client.run();
    }

    public static RemoteInterface initClient(String host, String companyName) {

        System.setSecurityManager(null);

        try {
            Registry registry = LocateRegistry.getRegistry(host);
            RemoteInterface carRentalCompany = (RemoteInterface) registry.lookup(companyName);
            return carRentalCompany;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	
	/***************
	 * CONSTRUCTOR *
	 ***************/
	
	public Client(String scriptFile, String carRentalCompanyName) {
		super(scriptFile);
		// TODO Auto-generated method stub
		this.rentalCarCompany = initClient("localhost", carRentalCompanyName);
	}

	private RemoteInterface rentalCarCompany;

	private RemoteInterface getCarRentalCompany() {
	    return this.rentalCarCompany;
    }
	
	/**
	 * Check which car types are available in the given period
	 * and print this list of car types.
	 *
	 * @param 	start
	 * 			start time of the period
	 * @param 	end
	 * 			end time of the period
	 * @throws 	Exception
	 * 			if things go wrong, throw exception
	 */
	@Override
	protected void checkForAvailableCarTypes(Date start, Date end) throws Exception {
		// TODO Auto-generated method stub
        Set<CarType> availableCarTypes = getCarRentalCompany().getAvailableCarTypes(start, end);
        for (CarType carType : availableCarTypes) {
            System.out.println(carType);
        }
	}

	/**
	 * Retrieve a quote for a given car type (tentative reservation).
	 * 
	 * @param	clientName 
	 * 			name of the client 
	 * @param 	start 
	 * 			start time for the quote
	 * @param 	end 
	 * 			end time for the quote
	 * @param 	carType 
	 * 			type of car to be reserved
	 * @param 	region
	 * 			region in which car must be available
	 * @return	the newly created quote
	 *  
	 * @throws 	Exception
	 * 			if things go wrong, throw exception
	 */
	@Override
	protected Quote createQuote(String clientName, Date start, Date end,
			String carType, String region) throws Exception {
		// TODO Auto-generated method stub
        ReservationConstraints reservationConstraints = new ReservationConstraints(start, end, carType, region);
        return getCarRentalCompany().createQuote(reservationConstraints, clientName);
	}

	/**
	 * Confirm the given quote to receive a final reservation of a car.
	 * 
	 * @param 	quote 
	 * 			the quote to be confirmed
	 * @return	the final reservation of a car
	 * 
	 * @throws 	Exception
	 * 			if things go wrong, throw exception
	 */
	@Override
	protected Reservation confirmQuote(Quote quote) throws Exception {
		// TODO Auto-generated method stub
        return getCarRentalCompany().confirmQuote(quote);
	}
	
	/**
	 * Get all reservations made by the given client.
	 *
	 * @param 	clientName
	 * 			name of the client
	 * @return	the list of reservations of the given client
	 * 
	 * @throws 	Exception
	 * 			if things go wrong, throw exception
	 */
	@Override
	protected List<Reservation> getReservationsByRenter(String clientName) throws Exception {
		// TODO Auto-generated method stub
        return getCarRentalCompany().getAllReservationsForClient(clientName);
	}

	/**
	 * Get the number of reservations for a particular car type.
	 * 
	 * @param 	carType 
	 * 			name of the car type
	 * @return 	number of reservations for the given car type
	 * 
	 * @throws 	Exception
	 * 			if things go wrong, throw exception
	 */
	@Override
	protected int getNumberOfReservationsForCarType(String carType) throws Exception {
		// TODO Auto-generated method stub
        return getCarRentalCompany().getReservationsCountForCarType(carType);
	}
}