const { expect } = require("chai");
const { ethers } = require("hardhat");
describe("SimpleStore Contract", function () {
    let SimpleStore;
    let simpleStore;
    let owner;
    let addr1;
    let addr2;
    beforeEach(async function () {
        SimpleStore = await ethers.getContractFactory("SimpleStore");
        [owner, addr1, addr2, _] = await ethers.getSigners();
        simpleStore = await SimpleStore.deploy({ value: ethers.utils.parseEther("1") });
        await simpleStore.deployed();
    });
    it("Should set the right owner", async function () {
        expect(await simpleStore.owner()).to.equal(owner.address);
    });
    it("Owner can add a product", async function () {
        await expect(simpleStore.addProduct("Apple", ethers.utils.parseEther("0.01"), 100))
            .to.emit(simpleStore, "ProductAdded")
            .withArgs(1, "Apple", ethers.utils.parseEther("0.01"), 100);
        const product = await simpleStore.products(1);
        expect(product.name).to.equal("Apple");
        expect(product.price).to.equal(ethers.utils.parseEther("0.01"));
        expect(product.quantity).to.equal(100);
    });
    it("Non-owner cannot add a product", async function () {
        await expect(
            simpleStore.connect(addr1).addProduct("Banana", ethers.utils.parseEther("0.02"), 50)
        ).to.be.revertedWith("Only the owner can call this function");
    });
    it("User can purchase a product", async function () {
        await simpleStore.addProduct("Orange", ethers.utils.parseEther("0.01"), 100);

        await expect(
            simpleStore.connect(addr1).purchaseProduct(1, 10, {
                value: ethers.utils.parseEther("0.1"),
            })
        )
            .to.emit(simpleStore, "ProductPurchased")
            .withArgs(1, 10, ethers.utils.parseEther("0.1"));
        const product = await simpleStore.products(1);
        expect(product.quantity).to.equal(90);
        const ownerBalance = await simpleStore.balances(owner.address);
        expect(ownerBalance).to.equal(ethers.utils.parseEther("0.1"));
    });
    it("Should fail if not enough Ether is sent", async function () {
        await simpleStore.addProduct("Grapes", ethers.utils.parseEther("0.05"), 50);
        await expect(
            simpleStore.connect(addr1).purchaseProduct(1, 1, {
                value: ethers.utils.parseEther("0.01"),
            })
        ).to.be.revertedWith("Insufficient funds sent");
    });
});
