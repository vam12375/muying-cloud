# final_review_gate.py
import sys
import os

if __name__ == "__main__":
    
    try:
        sys.stdout = os.fdopen(sys.stdout.fileno(), 'w', buffering=1)
        sys.stderr = os.fdopen(sys.stderr.fileno(), 'w', buffering=1)
    except Exception:
        pass 

    print("Review Gate: Current step completed. Please enter your instructions for [this step] (or enter keywords like 'complete', 'next' to end review of this step):", flush=True) 
    
    active_session = True
    while active_session:
        try:
            
            line = sys.stdin.readline()
            
            if not line:  # EOF
                print("--- REVIEW GATE: STDIN closed (EOF), exiting script ---", flush=True) 
                active_session = False
                break
            
            user_input = line.strip()
            
            user_input_lower = user_input.lower() # Convert English input to lowercase for case-insensitive matching
            
            # Keywords to end current step review
            english_exit_keywords = [
                'task_complete', 'continue', 'next', 'end', 'complete', 'endtask', 'continue_task', 'end_task'
            ]
            chinese_exit_keywords = [
                '没问题', '继续', '下一步', '完成', '结束任务', '结束'
            ]
            
            is_exit_keyword_detected = False
            if user_input_lower in english_exit_keywords:
                is_exit_keyword_detected = True
            else:
                for ch_keyword in chinese_exit_keywords: # Exact match for Chinese keywords
                    if user_input == ch_keyword:
                        is_exit_keyword_detected = True
                        break
                        
            if is_exit_keyword_detected:
                print(f"--- REVIEW GATE: User ended review of [this step] through '{user_input}' ---", flush=True) 
                active_session = False
                break
            elif user_input: 
                print(f"USER_REVIEW_SUB_PROMPT: {user_input}", flush=True) # AI needs to monitor this format
            
        except KeyboardInterrupt:
            print("--- REVIEW GATE: User interrupted review of [this step] through Ctrl+C ---", flush=True) 
            active_session = False
            break
        except Exception as e:
            print(f"--- REVIEW GATE [this step] script error: {e} ---", flush=True) 
            active_session = False
            break