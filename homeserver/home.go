package main

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"net/http"
	"os"
	"os/exec"
	"strconv"
	"time"

	"github.com/lair-framework/go-nmap"
)

const FILE = "devices.xml"
const NMAP_CMD = "sudo nmap -sn  192.168.1.63-253 -oX " + FILE + " > /dev/null"
const ENDPOINT = "/api/home/online"

func main() {
	// For testing:
	// export SERVER="http://localhost"
	// export USER="test"
	// export SCAN_INTERVAL=1000
	user := os.Getenv("USER")
	server := os.Getenv("SERVER")
	interval, _ := strconv.Atoi(os.Getenv("SCAN_INTERVAL")) // In seconds

	for {
		scan()
		sendToServer(server, user)
		time.Sleep(time.Duration(interval) * time.Second)
	}
}

func scan() {
	exec.Command("/bin/sh", "-c", NMAP_CMD).Output()
	fmt.Print("Scan completed!\n")
}

func sendToServer(server string, user string) {
	// Retrieve nmap xml-file
	dat, _ := ioutil.ReadFile(FILE)
	xmlResults, _ := nmap.Parse(dat)

	// Construct request
	macRecording := new(MacRecording)
	macRecording.Timestamp = time.Now().Unix()
	n := len(xmlResults.Hosts)
	macRecording.Macs = make([]string, n, 2*n)
	i := 0
	for key := range xmlResults.Hosts {
		addresses := xmlResults.Hosts[key].Addresses
		if len(addresses) > 1 {
			fmt.Printf("%s\n", xmlResults.Hosts[key].Addresses[1].Addr)
			macRecording.Macs[i] = addresses[1].Addr
			i += 1
		}
	}
	jsonBody, err := json.Marshal(macRecording)

	if err != nil {
		fmt.Println(err)
		return
	}

	// Send request
	fmt.Println("Payload: " + string(jsonBody))
	req, _ := http.NewRequest("POST", server+ENDPOINT, bytes.NewReader(jsonBody))
	req.Header.Add("user", user)
	client := &http.Client{}
	resp, err := client.Do(req)
	if err != nil {
		fmt.Printf("Error occured during sending: %s\n", err)
	} else {
		fmt.Printf("Send to the server: %s\n", resp)
	}
}

type MacRecording struct {
	Macs      []string
	Timestamp int64
}
