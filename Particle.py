#!/usr/bin/env python3

# Particles - zFa3
# July 10, 2024

import tkinter as tk
import math as Mt
import random as Rd

class GUI:
    def __init__(self) -> None:
        # dimensions of the pool table
        # measured inwidth, height
        self.dimensions = [1000, 500]
        self.root = tk.Tk()
        self.root.geometry(f"{self.dimensions[0]}x{self.dimensions[1]}")
        self.game_canvas = tk.Canvas(self.root, width=self.dimensions[0], height= self.dimensions[1])
        self.root.bind("<Configure>", self.resize)

        # Friction coefficient
        self.FrictionCOF = 0.999
        self.ballRadius = 15 # measured in pixels (px)
        self.numBalls =  250
        # list of all the balls
        # spacing out all the balls
        # Each Ball has 4 properties - > speed, x location, y location, and angle (from horizontal right line)
        # Speed should range from 0 -> 25

        self.tolerance = 3
        self.balls = [[10, [self.dimensions[0]//2, self.dimensions[1]//2], Rd.randint(0, 360)] for x in range(self.numBalls)]
        # [x * (self.ballRadius * 2) + 25, x * (self.ballRadius * 2) + 25]

        # colors of the balls
        self.colors = [
            "#FFFF00",  # Yellow
            "#0000FF",  # Blue
            "#FF0000",  # Red
            "#800080",  # Purple
            "#FFA500",  # Orange
            "#008000",  # Green
            "#800000",  # Maroon/Brown
            "#000000",  # Black
        ]

    def resize(self, _):
        self.dimensions = [self.root.winfo_width(), self.root.winfo_height()]
        self.game_canvas.config(width=self.dimensions[0], height=self.dimensions[1])

    def draw(self):
        self.game_canvas.delete("all")
        for i, t in enumerate(self.balls):
            self.game_canvas.create_oval(t[1][0] - self.ballRadius, t[1][1] - self.ballRadius, t[1][0] + self.ballRadius, t[1][1] + self.ballRadius, fill = self.colors[i%len(self.colors)], outline="")
        self.game_canvas.update()
        self.game_canvas.pack()

    def Gameloop(self):
        while True:
            for i, t in enumerate(self.balls):
                ct = 0
                ###########################################################
                # Slow all the balls down per tick
                ###########################################################
                if t[0] * self.FrictionCOF < 1e-5:
                    self.balls[i][0] = 0
                else:
                    self.balls[i][0] = t[0] * self.FrictionCOF
                rad = Mt.radians(self.balls[i][2])
                self.balls[i][1][0] += Mt.cos(rad) * self.balls[i][0]
                self.balls[i][1][1] += Mt.sin(rad) * self.balls[i][0]

                ###########################################################
                # Check if they can bounce off walls
                ###########################################################

                if abs(0 - self.balls[i][1][0]) < self.ballRadius:
                    self.balls[i][2] = abs(90-self.balls[i][2] + Rd.randint(-15, 15))
                    #print(self.balls[i][0])
                    ct += 1
                #if abs(self.dimensions[0] - self.balls[i][1][0]) < self.ballRadius:
                if self.dimensions[0] < self.balls[i][1][0] + self.ballRadius:
                    self.balls[i][2] = abs(180-self.balls[i][2] + Rd.randint(-15, 15))
                    #print(self.balls[i][0])
                    ct += 1
                #if abs(0 - self.balls[i][1][1]) < self.ballRadius:
                if 0 > self.balls[i][1][1] - self.ballRadius:
                    self.balls[i][2] = abs(90-self.balls[i][2] + Rd.randint(-15, 15))
                    #print(self.balls[i][0])
                    ct += 1
                #if abs(self.dimensions[1] - self.balls[i][1][1]) < self.ballRadius:
                if self.dimensions[1] < self.balls[i][1][1] + self.ballRadius:
                    self.balls[i][2] = abs(360-self.balls[i][2] + Rd.randint(-15, 15))
                    #print(self.balls[i][0])
                    ct += 1
                if self.balls[i][1][1] + self.balls[i][1][0] > sum(self.dimensions) + self.tolerance or ct > 2:
                    self.balls[i][1] = [self.dimensions[0]//2, self.dimensions[1]//2]
            self.draw()

Game = GUI()
Game.draw()
Game.Gameloop()
