package ua.store.dao;

public class DAOFactory {

    private static volatile DAOFactory instance;

    private final ProductDAO productDAO;
    private final CategoryDAO categoryDAO;

    private DAOFactory() {
        this.productDAO = ProductDAOImpl.getInstance();
        this.categoryDAO = CategoryDAOImpl.getInstance();
    }

    public static DAOFactory getInstance() {
        if (instance == null) {
            synchronized (DAOFactory.class) {
                if (instance == null) {
                    instance = new DAOFactory();
                }
            }
        }
        return instance;
    }

    public ProductDAO createProductDAO() {
        return productDAO;
    }

    public CategoryDAO createCategoryDAO() {
        return categoryDAO;
    }
}
