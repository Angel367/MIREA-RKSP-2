pragma solidity ^0.8.0;
contract SimpleStore {
    address public owner;
    mapping(address => uint256) public balances;
    mapping(uint256 => Product) public products;
    uint256 public productCount = 0;
    struct Product {
        string name;
        uint256 price;
        uint256 quantity;
    }
    event ProductAdded(uint256 productId, string name, uint256 price, uint256 quantity);
    event ProductPurchased(uint256 productId, uint256 quantity, uint256 totalCost);
    modifier onlyOwner() {
        require(msg.sender == owner, "Only the owner can call this function");
        _;
    }
    constructor() payable {
        owner = msg.sender;
    }
    function addProduct(string memory _name, uint256 _price, uint256 _quantity) public onlyOwner {
        require(_price > 0, "Price must be greater than zero");
        require(_quantity > 0, "Quantity must be greater than zero");
        productCount++;
        products[productCount] = Product(_name, _price, _quantity);
        emit ProductAdded(productCount, _name, _price, _quantity);
    }
    function purchaseProduct(uint256 _productId, uint256 _quantity) public payable {
        require(_productId > 0 && _productId <= productCount, "Invalid product ID");
        Product storage product = products[_productId];
        require(product.quantity >= _quantity, "Not enough stock available");
        uint256 totalCost = product.price * _quantity;
        require(msg.value >= totalCost, "Insufficient funds sent");
        balances[owner] += totalCost;
        product.quantity -= _quantity;
        emit ProductPurchased(_productId, _quantity, totalCost);
        if (msg.value > totalCost) {
            payable(msg.sender).transfer(msg.value - totalCost);
        }
    }
    function withdrawBalance() public onlyOwner {
        uint256 balance = balances[owner];
        require(balance > 0, "No balance to withdraw");
        // Сбрасываем баланс до перевода
        balances[owner] = 0;
        // Переводим баланс владельцу
        payable(owner).transfer(balance);
    }
}