// DO NOT USE THIS / ONLY FOR TESTING


var text = "!§$%&/()²[]³@~+*<>|-#=?´öä_.:s°^";

// type A = AES; type B = XOR
function encrypt(str, type) {
    switch (type) {
        case "A":
            // Key
            var secret = "Jupiter";
            var encrypted = CryptoJS.AES.encrypt(str, secret);
            encrypted = encrypted.toString();
            return "A" + encrypted;
        case "B":
            var encoded = "";
            for (i = 0; i < str.length; i++) {
                var a = str.charCodeAt(i);
                var b = a ^ 1;
                encoded = encoded + String.fromCharCode(b);
            }
            return "B" + encoded;
    }
}

console.log("Cipher text A: " + encrypt(text, "A"));
console.log("Cipher text B: " + encrypt(text, "B"));
document.getElementById("output").innerHTML = "Output in Console";
