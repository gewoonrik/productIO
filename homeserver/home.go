package main

import (
	"bytes"
	"net/http"
	"os"
	"os/exec"
	"strconv"
	"time"

	"github.com/lair-framework/go-nmap"
)

import "encoding/json"
import "fmt"
import "io/ioutil"

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
	macRecording.timestamp = time.Now().Unix()
	n := len(xmlResults.Hosts)
	macRecording.macs = make([]string, n, 2*n)
	i := 0
	for key := range xmlResults.Hosts {
		addresses := xmlResults.Hosts[key].Addresses
		if len(addresses) > 1 {
			fmt.Printf("%s\n", xmlResults.Hosts[key].Addresses[1].Addr)
			macRecording.macs[i] = addresses[1].Addr
			i += 1
		}
	}
	jsonBody, _ := json.Marshal(macRecording)

	// Send request
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
	macs      []string
	timestamp int64
}
