package com.phoenix.blocker.dao;

public class Owner {

        private String owner;
        private String address;
        private String nic;

        public Owner() {
        }

        public Owner(String owner, String address, String nic) {
            this.owner = owner;
            this.address = address;
            this.nic = nic;
        }

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getNic() {
            return nic;
        }

        public void setNic(String nic) {
            this.nic = nic;
        }

}
